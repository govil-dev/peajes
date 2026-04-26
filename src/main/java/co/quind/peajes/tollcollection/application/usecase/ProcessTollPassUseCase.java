package co.quind.peajes.tollcollection.application.usecase;

import co.quind.peajes.tollcollection.application.command.ProcessTollPassCommand;
import co.quind.peajes.tollcollection.application.dto.AccountDetails;
import co.quind.peajes.tollcollection.application.dto.TollPassResult;
import co.quind.peajes.tollcollection.domain.event.AccountBalanceLow;
import co.quind.peajes.tollcollection.domain.event.TollPassRegistered;
import co.quind.peajes.tollcollection.domain.event.TransactionAuthorized;
import co.quind.peajes.tollcollection.domain.event.TransactionDeclined;
import co.quind.peajes.tollcollection.domain.model.DeclineReason;
import co.quind.peajes.tollcollection.domain.model.TariffConfig;
import co.quind.peajes.tollcollection.domain.model.TollPass;
import co.quind.peajes.tollcollection.domain.model.VehicleClass;
import co.quind.peajes.tollcollection.domain.port.out.*;
import co.quind.peajes.tollcollection.domain.valueobject.*;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.TimeoutException;

/**
 * Caso de uso: Procesar paso vehicular por peaje (HU-001).
 * Orquesta validación de carril, tag, tarifa y débito de saldo con idempotencia por passId.
 */
@Service
public class ProcessTollPassUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessTollPassUseCase.class);

    private final TollPassRepository tollPassRepository;
    private final TariffRepository tariffRepository;
    private final LaneRepository laneRepository;
    private final AccountManagementPort accountManagementPort;
    private final EventPublisherPort eventPublisher;
    private final BarrierControlPort barrierControl;
    private final MoneyAmount balanceAlertThreshold;

    public ProcessTollPassUseCase(
            TollPassRepository tollPassRepository,
            TariffRepository tariffRepository,
            LaneRepository laneRepository,
            AccountManagementPort accountManagementPort,
            EventPublisherPort eventPublisher,
            BarrierControlPort barrierControl,
            @Value("${toll.balance-alert-threshold}") String alertThreshold,
            @Value("${toll.balance-alert-currency}") String alertCurrency) {
        this.tollPassRepository = tollPassRepository;
        this.tariffRepository = tariffRepository;
        this.laneRepository = laneRepository;
        this.accountManagementPort = accountManagementPort;
        this.eventPublisher = eventPublisher;
        this.barrierControl = barrierControl;
        this.balanceAlertThreshold = MoneyAmount.of(alertThreshold, alertCurrency);
    }

    /**
     * Procesa un paso vehicular. Garantiza idempotencia: si el passId ya fue procesado,
     * retorna el resultado original sin cobrar de nuevo ni emitir eventos duplicados.
     */
    public Mono<TollPassResult> process(ProcessTollPassCommand command) {
        var tagId = new TagId(command.tagId());
        var stationId = new StationId(command.stationId());
        var laneId = new LaneId(command.laneId());
        var vehicleClass = VehicleClass.valueOf(command.vehicleClass());
        var passId = PassId.of(tagId, command.detectedAt());

        setupMdc(passId, tagId, stationId);

        return tollPassRepository.findByPassId(passId)
                .doOnNext(existing -> log.info("passId duplicado detectado — retornando resultado en caché"))
                .map(TollPassResult::fromExisting)
                .switchIfEmpty(processNew(passId, tagId, stationId, laneId, vehicleClass, command.detectedAt()))
                .doFinally(signal -> MDC.clear());
    }

    private Mono<TollPassResult> processNew(PassId passId, TagId tagId, StationId stationId,
                                             LaneId laneId, VehicleClass vehicleClass, Instant detectedAt) {
        return laneRepository.findByLaneIdAndStationId(laneId, stationId)
                .flatMap(lane -> {
                    if (!lane.isOpen()) {
                        log.warn("Carril {} no está OPEN (status={})", laneId.value(), lane.status());
                        return declineAndPersist(passId, tagId, stationId, laneId, vehicleClass,
                                MoneyAmount.of("0.00", "COP"), DeclineReason.LANE_CLOSED, detectedAt);
                    }
                    return tariffRepository.findActiveByStationAndVehicleClass(stationId, vehicleClass)
                            .flatMap(tariff -> authorizeWithAccount(passId, tagId, stationId, laneId,
                                    vehicleClass, tariff, detectedAt));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("Carril no encontrado: stationId={} laneId={}", stationId.value(), laneId.value());
                    return declineAndPersist(passId, tagId, stationId, laneId, vehicleClass,
                            MoneyAmount.of("0.00", "COP"), DeclineReason.SYSTEM_ERROR, detectedAt);
                }));
    }

    private Mono<TollPassResult> authorizeWithAccount(PassId passId, TagId tagId, StationId stationId,
                                                       LaneId laneId, VehicleClass vehicleClass,
                                                       TariffConfig tariff, Instant detectedAt) {
        return accountManagementPort.getAccountByTag(tagId)
                .flatMap(account -> evaluateAccount(passId, tagId, stationId, laneId, vehicleClass,
                        tariff, account, detectedAt))
                .onErrorResume(CallNotPermittedException.class, e -> {
                    log.error("Circuit breaker ABIERTO para account-management-api — declinando con SYSTEM_ERROR");
                    return declineAndPersist(passId, tagId, stationId, laneId, vehicleClass,
                            tariff.amount(), DeclineReason.SYSTEM_ERROR, detectedAt);
                })
                .onErrorResume(TimeoutException.class, e -> {
                    log.error("Timeout en account-management-api — declinando con SYSTEM_ERROR");
                    return declineAndPersist(passId, tagId, stationId, laneId, vehicleClass,
                            tariff.amount(), DeclineReason.SYSTEM_ERROR, detectedAt);
                });
    }

    private Mono<TollPassResult> evaluateAccount(PassId passId, TagId tagId, StationId stationId,
                                                  LaneId laneId, VehicleClass vehicleClass,
                                                  TariffConfig tariff, AccountDetails account,
                                                  Instant detectedAt) {
        switch (account.tagStatus()) {
            case INACTIVE, SUSPENDED -> {
                log.warn("Tag {} inactivo/suspendido — declinando paso", tagId.toMasked());
                return declineAndPersist(passId, tagId, stationId, laneId, vehicleClass,
                        tariff.amount(), DeclineReason.TAG_INACTIVE, detectedAt);
            }
            default -> {
                if (account.balance().isLessThan(tariff.amount())) {
                    log.info("Saldo insuficiente para tag {} — declinando y notificando", tagId.toMasked());
                    return declineInsufficientBalance(passId, tagId, stationId, laneId, vehicleClass,
                            tariff.amount(), account, detectedAt);
                }
                return deductAndAuthorize(passId, tagId, stationId, laneId, vehicleClass,
                        tariff.amount(), detectedAt);
            }
        }
    }

    private Mono<TollPassResult> deductAndAuthorize(PassId passId, TagId tagId, StationId stationId,
                                                     LaneId laneId, VehicleClass vehicleClass,
                                                     MoneyAmount amount, Instant detectedAt) {
        return accountManagementPort.deductBalance(tagId, amount, passId)
                .flatMap(newBalance -> {
                    var tollPass = TollPass.authorized(passId, tagId, stationId, laneId, vehicleClass, amount, detectedAt);
                    return tollPassRepository.save(tollPass)
                            .flatMap(saved -> eventPublisher.publishToTransactions(
                                    TollPassRegistered.from(passId, tagId, stationId, laneId, vehicleClass, amount, detectedAt))
                                    .then(eventPublisher.publishToTransactions(
                                            TransactionAuthorized.from(passId, tagId, amount, newBalance, saved.processedAt())))
                                    .then(barrierControl.openBarrier(laneId))
                                    .then(publishBalanceLowIfNeeded(tagId, newBalance, detectedAt))
                                    .thenReturn(TollPassResult.authorized(saved)));
                });
    }

    private Mono<TollPassResult> declineInsufficientBalance(PassId passId, TagId tagId, StationId stationId,
                                                             LaneId laneId, VehicleClass vehicleClass,
                                                             MoneyAmount amount, AccountDetails account,
                                                             Instant detectedAt) {
        var tollPass = TollPass.declined(passId, tagId, stationId, laneId, vehicleClass,
                amount, DeclineReason.INSUFFICIENT_BALANCE, detectedAt);
        return tollPassRepository.save(tollPass)
                .flatMap(saved -> eventPublisher.publishToTransactions(
                        TransactionDeclined.from(passId, tagId, amount,
                                DeclineReason.INSUFFICIENT_BALANCE, saved.processedAt()))
                        .then(barrierControl.notifyOperator(laneId, "INSUFFICIENT_BALANCE"))
                        .then(publishBalanceLowIfNeeded(tagId, account.balance(), detectedAt))
                        .thenReturn(TollPassResult.declined(saved)));
    }

    private Mono<TollPassResult> declineAndPersist(PassId passId, TagId tagId, StationId stationId,
                                                    LaneId laneId, VehicleClass vehicleClass,
                                                    MoneyAmount amount, DeclineReason reason,
                                                    Instant detectedAt) {
        var tollPass = TollPass.declined(passId, tagId, stationId, laneId, vehicleClass, amount, reason, detectedAt);
        return tollPassRepository.save(tollPass)
                .flatMap(saved -> eventPublisher.publishToTransactions(
                        TransactionDeclined.from(passId, tagId, amount, reason, saved.processedAt()))
                        .thenReturn(TollPassResult.declined(saved)));
    }

    private Mono<Void> publishBalanceLowIfNeeded(TagId tagId, MoneyAmount currentBalance, Instant occurredAt) {
        if (currentBalance.isLessThan(balanceAlertThreshold)) {
            return eventPublisher.publishToAccounts(
                    AccountBalanceLow.from(tagId, currentBalance, balanceAlertThreshold, occurredAt));
        }
        return Mono.empty();
    }

    private void setupMdc(PassId passId, TagId tagId, StationId stationId) {
        MDC.put("correlationId", passId.value());
        MDC.put("tagId", tagId.toMasked());
        MDC.put("stationId", stationId.value());
    }
}
