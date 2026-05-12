package co.quind.peajes.tollcollection.application.usecase;

import co.quind.peajes.tollcollection.application.command.ProcessTollPassCommand;
import co.quind.peajes.tollcollection.application.dto.AccountDetails;
import co.quind.peajes.tollcollection.application.dto.TollPassResult;
import co.quind.peajes.tollcollection.domain.event.AccountBalanceLow;
import co.quind.peajes.tollcollection.domain.model.*;
import co.quind.peajes.tollcollection.domain.port.in.ProcessTollPassUseCase;
import co.quind.peajes.tollcollection.domain.port.out.*;
import co.quind.peajes.tollcollection.domain.valueobject.*;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class ProcessTollPassService implements ProcessTollPassUseCase {

	private final TollPassRepository tollPassRepository;
	private final TariffRepository tariffRepository;
	private final LaneRepository laneRepository;
	private final AccountManagementPort accountManagementPort;
	private final EventPublisherPort eventPublisher;
	private final BarrierControlPort barrierControl;
	private final MoneyAmount balanceAlertThreshold;

	public ProcessTollPassService(
			TollPassRepository tollPassRepository,
			TariffRepository tariffRepository,
			LaneRepository laneRepository,
			AccountManagementPort accountManagementPort,
			EventPublisherPort eventPublisher,
			BarrierControlPort barrierControl,
			@Value("${toll.balance.alert.threshold:15000.00}") String thresholdAmount,
			@Value("${toll.balance.alert.currency:COP}") String thresholdCurrency) {
		this.tollPassRepository = tollPassRepository;
		this.tariffRepository = tariffRepository;
		this.laneRepository = laneRepository;
		this.accountManagementPort = accountManagementPort;
		this.eventPublisher = eventPublisher;
		this.barrierControl = barrierControl;
		this.balanceAlertThreshold = MoneyAmount.of(thresholdAmount, thresholdCurrency);
	}

	@Override
	public Mono<TollPassResult> process(ProcessTollPassCommand command) {
		PassId passId = PassId.of(TagId.of(command.tagId()), command.detectedAt());
		TagId tagId = TagId.of(command.tagId());
		StationId stationId = StationId.of(command.stationId());
		LaneId laneId = LaneId.of(command.laneId());
		VehicleClass vehicleClass = VehicleClass.valueOf(command.vehicleClass());

		log.info("Processing toll pass: passId={}, tagId={}", passId, tagId.toMasked());

		return tollPassRepository.findByPassId(passId)
			.map(existing -> {
				log.info("TollPass already processed (idempotent): passId={}", passId);
				return TollPassResult.fromExisting(existing);
			})
			.switchIfEmpty(Mono.defer(() -> processNew(passId, tagId, stationId, laneId, vehicleClass, command)));
	}

	private Mono<TollPassResult> processNew(PassId passId, TagId tagId, StationId stationId,
											LaneId laneId, VehicleClass vehicleClass,
											ProcessTollPassCommand command) {
		return laneRepository.findByLaneIdAndStationId(laneId, stationId)
			.flatMap(lane -> isLaneBlocked(lane)
				? handleLaneBlocked(passId, tagId, stationId, laneId, command)
				: handleOpenLane(passId, tagId, stationId, laneId, vehicleClass, command));
	}

	private boolean isLaneBlocked(Lane lane) {
		return !lane.isOpen();
	}

	private Mono<TollPassResult> handleLaneBlocked(PassId passId, TagId tagId, StationId stationId,
													LaneId laneId, ProcessTollPassCommand command) {
		log.warn("Lane not open: laneId={}", laneId);
		return saveAndPublish(TollPass.decline(passId, tagId, stationId, laneId,
			DeclineReason.LANE_NOT_OPEN, command.detectedAt()));
	}

	private Mono<TollPassResult> handleOpenLane(PassId passId, TagId tagId, StationId stationId,
												 LaneId laneId, VehicleClass vehicleClass,
												 ProcessTollPassCommand command) {
		return tariffRepository.findActiveByStationAndVehicleClass(stationId, vehicleClass)
			.flatMap(tariff -> accountManagementPort.getAccountByTag(tagId)
				.flatMap(account -> evaluateAccount(passId, tagId, stationId, laneId, vehicleClass, command, tariff, account))
				.onErrorResume(ex -> ex instanceof CallNotPermittedException || ex instanceof TimeoutException,
					ex -> handleAccountServiceError(passId, tagId, stationId, laneId, command, ex)));
	}

	private Mono<TollPassResult> evaluateAccount(PassId passId, TagId tagId, StationId stationId,
												  LaneId laneId, VehicleClass vehicleClass,
												  ProcessTollPassCommand command,
												  TariffConfig tariff,
												  AccountDetails account) {
		if (account.tagStatus() == TagStatus.INACTIVE || account.tagStatus() == TagStatus.SUSPENDED) {
			log.warn("Tag inactive/suspended: tagId={}", tagId.toMasked());
			return saveAndPublish(TollPass.decline(passId, tagId, stationId, laneId,
				DeclineReason.TAG_INACTIVE, command.detectedAt()));
		}
		if (account.balance().isLessThan(tariff.amount())) {
			log.warn("Insufficient balance: tagId={}", tagId.toMasked());
			TollPass declined = TollPass.declined(passId, tagId, stationId, laneId,
				vehicleClass, tariff.amount(), DeclineReason.INSUFFICIENT_BALANCE, command.detectedAt());
			return saveAndPublish(declined)
				.flatMap(result -> notifyOperator(laneId, "INSUFFICIENT_BALANCE").thenReturn(result));
		}
		return deductAndAuthorize(passId, tagId, stationId, laneId, vehicleClass, command, tariff, account.accountId());
	}

	private Mono<TollPassResult> deductAndAuthorize(PassId passId, TagId tagId, StationId stationId,
													 LaneId laneId, VehicleClass vehicleClass,
													 ProcessTollPassCommand command,
													 TariffConfig tariff, String accountId) {
		return accountManagementPort.deductBalance(tagId, tariff.amount(), passId)
			.flatMap(newBalance -> {
				log.info("Balance deducted: tagId={}, balanceAfter={}", tagId.toMasked(), newBalance.toDisplayString());
				TollPass authorized = TollPass.authorize(passId, tagId, stationId, laneId, vehicleClass,
					tariff.amount(), command.detectedAt(), accountId, newBalance.toJsonString());
				return saveAndPublish(authorized)
					.flatMap(result -> barrierControl.openBarrier(laneId)
						.then(alertIfLowBalance(newBalance, tagId))
						.thenReturn(result));
			});
	}

	private Mono<TollPassResult> handleAccountServiceError(PassId passId, TagId tagId, StationId stationId,
														   LaneId laneId, ProcessTollPassCommand command,
														   Throwable ex) {
		log.error("Account service unavailable ({}): passId={}", ex.getClass().getSimpleName(), passId);
		return saveAndPublish(TollPass.decline(passId, tagId, stationId, laneId,
			DeclineReason.SYSTEM_ERROR, command.detectedAt()));
	}

	private Mono<TollPassResult> saveAndPublish(TollPass tollPass) {
		var events = tollPass.pullDomainEvents();
		return tollPassRepository.save(tollPass)
			.doOnNext(saved -> log.info("TollPass saved: passId={}, status={}", saved.passId(), saved.status()))
			.flatMap(saved -> publishEvents(events).thenReturn(saved))
			.map(saved -> saved.status() == TransactionStatus.AUTHORIZED
				? TollPassResult.authorized(saved)
				: TollPassResult.declined(saved))
			.doOnError(ex -> log.error("Error saving/publishing toll pass", ex));
	}

	private Mono<Void> publishEvents(java.util.List<Object> events) {
		return reactor.core.publisher.Flux.fromIterable(events)
			.flatMap(event -> event instanceof AccountBalanceLow
				? eventPublisher.publishToAccounts(event)
				: eventPublisher.publishToTransactions(event))
			.then();
	}

	private Mono<Void> notifyOperator(LaneId laneId, String reason) {
		return barrierControl.notifyOperator(laneId, reason)
			.doOnError(ex -> log.warn("Failed to notify operator: laneId={}", laneId));
	}

	private Mono<Void> alertIfLowBalance(MoneyAmount balance, TagId tagId) {
		if (balance.isLessThan(balanceAlertThreshold)) {
			return eventPublisher.publishToAccounts(new AccountBalanceLow(
				java.util.UUID.randomUUID().toString(),
				null,
				tagId.value(),
				balance.currency(),
				balanceAlertThreshold.amount().toPlainString(),
				java.time.Instant.now().toString()
			));
		}
		return Mono.empty();
	}
}
