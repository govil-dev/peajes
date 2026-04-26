package co.quind.peajes.tollcollection.application.usecase;

import co.quind.peajes.tollcollection.application.command.ProcessTollPassCommand;
import co.quind.peajes.tollcollection.application.dto.AccountDetails;
import co.quind.peajes.tollcollection.application.dto.TollPassResult;
import co.quind.peajes.tollcollection.domain.model.*;
import co.quind.peajes.tollcollection.domain.port.out.*;
import co.quind.peajes.tollcollection.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessTollPassUseCaseTest {

    @Mock private TollPassRepository tollPassRepository;
    @Mock private TariffRepository tariffRepository;
    @Mock private LaneRepository laneRepository;
    @Mock private AccountManagementPort accountManagementPort;
    @Mock private EventPublisherPort eventPublisher;
    @Mock private BarrierControlPort barrierControl;

    private ProcessTollPassUseCase useCase;

    private static final Instant DETECTED_AT = Instant.parse("2025-04-24T14:23:05Z");
    private static final MoneyAmount TARIFF = MoneyAmount.of("9500.00", "COP");
    private static final MoneyAmount BALANCE_SUFFICIENT = MoneyAmount.of("50000.00", "COP");
    private static final MoneyAmount BALANCE_INSUFFICIENT = MoneyAmount.of("5000.00", "COP");
    private static final MoneyAmount BALANCE_AFTER = MoneyAmount.of("40500.00", "COP");

    @BeforeEach
    void setUp() {
        useCase = new ProcessTollPassUseCase(
                tollPassRepository, tariffRepository, laneRepository,
                accountManagementPort, eventPublisher, barrierControl,
                "15000.00", "COP");
    }

    @Test
    void pasoExitosoConSaldoSuficiente() {
        var command = buildCommand();
        var lane = new Lane(new LaneId("LN-001"), new StationId("ST-BOG-001"), LaneStatus.OPEN);
        var tariff = new TariffConfig(new StationId("ST-BOG-001"), VehicleClass.CLASS_I, TARIFF);
        var account = new AccountDetails("ACC-001", new TagId("A1B2C3D4"), TagStatus.ACTIVE, BALANCE_SUFFICIENT);
        var tollPass = TollPass.authorized(PassId.of(new TagId("A1B2C3D4"), DETECTED_AT),
                new TagId("A1B2C3D4"), new StationId("ST-BOG-001"),
                new LaneId("LN-001"), VehicleClass.CLASS_I, TARIFF, DETECTED_AT);

        when(tollPassRepository.findByPassId(any())).thenReturn(Mono.empty());
        when(laneRepository.findByLaneIdAndStationId(any(), any())).thenReturn(Mono.just(lane));
        when(tariffRepository.findActiveByStationAndVehicleClass(any(), any())).thenReturn(Mono.just(tariff));
        when(accountManagementPort.getAccountByTag(any())).thenReturn(Mono.just(account));
        when(accountManagementPort.deductBalance(any(), any(), any())).thenReturn(Mono.just(BALANCE_AFTER));
        when(tollPassRepository.save(any())).thenReturn(Mono.just(tollPass));
        when(eventPublisher.publishToTransactions(any())).thenReturn(Mono.empty());
        when(barrierControl.openBarrier(any())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.process(command))
                .assertNext(result -> {
                    assertThat(result.status()).isEqualTo(TransactionStatus.AUTHORIZED);
                    assertThat(result.fromCache()).isFalse();
                })
                .verifyComplete();

        verify(barrierControl).openBarrier(any());
        verify(eventPublisher, atLeastOnce()).publishToTransactions(any());
    }

    @Test
    void pasoRechazadoPorSaldoInsuficiente() {
        var command = buildCommand();
        var lane = new Lane(new LaneId("LN-001"), new StationId("ST-BOG-001"), LaneStatus.OPEN);
        var tariff = new TariffConfig(new StationId("ST-BOG-001"), VehicleClass.CLASS_I, TARIFF);
        var account = new AccountDetails("ACC-001", new TagId("A1B2C3D4"), TagStatus.ACTIVE, BALANCE_INSUFFICIENT);
        var tollPass = TollPass.declined(PassId.of(new TagId("A1B2C3D4"), DETECTED_AT),
                new TagId("A1B2C3D4"), new StationId("ST-BOG-001"),
                new LaneId("LN-001"), VehicleClass.CLASS_I, TARIFF,
                DeclineReason.INSUFFICIENT_BALANCE, DETECTED_AT);

        when(tollPassRepository.findByPassId(any())).thenReturn(Mono.empty());
        when(laneRepository.findByLaneIdAndStationId(any(), any())).thenReturn(Mono.just(lane));
        when(tariffRepository.findActiveByStationAndVehicleClass(any(), any())).thenReturn(Mono.just(tariff));
        when(accountManagementPort.getAccountByTag(any())).thenReturn(Mono.just(account));
        when(tollPassRepository.save(any())).thenReturn(Mono.just(tollPass));
        when(eventPublisher.publishToTransactions(any())).thenReturn(Mono.empty());
        when(eventPublisher.publishToAccounts(any())).thenReturn(Mono.empty());
        when(barrierControl.notifyOperator(any(), any())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.process(command))
                .assertNext(result -> {
                    assertThat(result.status()).isEqualTo(TransactionStatus.DECLINED);
                    assertThat(result.declineReason()).isEqualTo(DeclineReason.INSUFFICIENT_BALANCE);
                })
                .verifyComplete();

        verify(barrierControl, never()).openBarrier(any());
        verify(barrierControl).notifyOperator(any(), eq("INSUFFICIENT_BALANCE"));
        verify(eventPublisher).publishToAccounts(any());
    }

    @Test
    void idempotenciaRetornaResultadoOriginal() {
        var command = buildCommand();
        var existingPass = TollPass.authorized(PassId.of(new TagId("A1B2C3D4"), DETECTED_AT),
                new TagId("A1B2C3D4"), new StationId("ST-BOG-001"),
                new LaneId("LN-001"), VehicleClass.CLASS_I, TARIFF, DETECTED_AT);

        when(tollPassRepository.findByPassId(any())).thenReturn(Mono.just(existingPass));

        StepVerifier.create(useCase.process(command))
                .assertNext(result -> {
                    assertThat(result.fromCache()).isTrue();
                    assertThat(result.status()).isEqualTo(TransactionStatus.AUTHORIZED);
                })
                .verifyComplete();

        verify(accountManagementPort, never()).getAccountByTag(any());
        verify(accountManagementPort, never()).deductBalance(any(), any(), any());
        verify(eventPublisher, never()).publishToTransactions(any());
    }

    @Test
    void pasoRechazadoPorCarrilCerrado() {
        var command = buildCommand();
        var lane = new Lane(new LaneId("LN-001"), new StationId("ST-BOG-001"), LaneStatus.MAINTENANCE);
        var tollPass = TollPass.declined(PassId.of(new TagId("A1B2C3D4"), DETECTED_AT),
                new TagId("A1B2C3D4"), new StationId("ST-BOG-001"),
                new LaneId("LN-001"), VehicleClass.CLASS_I, MoneyAmount.of("0.00", "COP"),
                DeclineReason.LANE_CLOSED, DETECTED_AT);

        when(tollPassRepository.findByPassId(any())).thenReturn(Mono.empty());
        when(laneRepository.findByLaneIdAndStationId(any(), any())).thenReturn(Mono.just(lane));
        when(tollPassRepository.save(any())).thenReturn(Mono.just(tollPass));
        when(eventPublisher.publishToTransactions(any())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.process(command))
                .assertNext(result -> {
                    assertThat(result.status()).isEqualTo(TransactionStatus.DECLINED);
                    assertThat(result.declineReason()).isEqualTo(DeclineReason.LANE_CLOSED);
                })
                .verifyComplete();
    }

    private ProcessTollPassCommand buildCommand() {
        return new ProcessTollPassCommand("A1B2C3D4", "ST-BOG-001", "LN-001", "CLASS_I", DETECTED_AT);
    }
}
