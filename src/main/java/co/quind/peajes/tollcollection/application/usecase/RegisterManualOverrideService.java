package co.quind.peajes.tollcollection.application.usecase;

import co.quind.peajes.tollcollection.application.command.RegisterManualOverrideCommand;
import co.quind.peajes.tollcollection.application.dto.ManualOverrideResponse;
import co.quind.peajes.tollcollection.domain.model.ManualOverride;
import co.quind.peajes.tollcollection.domain.model.OverrideReason;
import co.quind.peajes.tollcollection.domain.model.VehicleClass;
import co.quind.peajes.tollcollection.domain.port.in.RegisterManualOverrideUseCase;
import co.quind.peajes.tollcollection.domain.port.out.EventPublisherPort;
import co.quind.peajes.tollcollection.domain.port.out.LaneRepository;
import co.quind.peajes.tollcollection.domain.port.out.ManualOverrideRepository;
import co.quind.peajes.tollcollection.domain.port.out.StoragePort;
import co.quind.peajes.tollcollection.domain.port.out.TariffRepository;
import co.quind.peajes.tollcollection.domain.valueobject.LaneId;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class RegisterManualOverrideService implements RegisterManualOverrideUseCase {

	private final ManualOverrideRepository overrideRepository;
	private final LaneRepository laneRepository;
	private final TariffRepository tariffRepository;
	private final StoragePort storage;
	private final EventPublisherPort eventPublisher;

	public RegisterManualOverrideService(
			ManualOverrideRepository overrideRepository,
			LaneRepository laneRepository,
			TariffRepository tariffRepository,
			StoragePort storage,
			EventPublisherPort eventPublisher) {
		this.overrideRepository = overrideRepository;
		this.laneRepository = laneRepository;
		this.tariffRepository = tariffRepository;
		this.storage = storage;
		this.eventPublisher = eventPublisher;
	}

	@Override
	public Mono<ManualOverrideResponse> register(RegisterManualOverrideCommand command) {
		StationId stationId = StationId.of(command.stationId());
		LaneId laneId = LaneId.of(command.laneId());
		VehicleClass vehicleClass = VehicleClass.valueOf(command.vehicleClass());
		OverrideReason reason = OverrideReason.fromString(command.reason());

		log.info("Registering manual override: stationId={}, laneId={}, operatorId={}",
			stationId, laneId, command.operatorId());

		return validateLaneIsOpen(stationId, laneId)
			.flatMap(lane -> getTariffAmount(stationId, vehicleClass)
				.flatMap(tariff -> createAndSaveOverride(command, stationId, laneId, vehicleClass, reason, tariff)));
	}

	private Mono<Void> validateLaneIsOpen(StationId stationId, LaneId laneId) {
		return laneRepository.findByLaneIdAndStationId(laneId, stationId)
			.flatMap(lane -> {
				if (!lane.isOpen()) {
					log.warn("Lane not open for manual override: laneId={}, stationId={}", laneId, stationId);
					return Mono.<Void>error(new IllegalStateException("Lane is not in OPEN state"));
				}
				return Mono.<Void>empty();
			})
			.switchIfEmpty(Mono.error(new IllegalArgumentException("Lane not found")))
			.then();
	}

	private Mono<MoneyAmount> getTariffAmount(StationId stationId, VehicleClass vehicleClass) {
		return tariffRepository.findActiveByStationAndVehicleClass(stationId, vehicleClass)
			.map(tariff -> tariff.amount())
			.switchIfEmpty(Mono.error(new IllegalStateException("No active tariff found")));
	}

	private Mono<ManualOverrideResponse> createAndSaveOverride(RegisterManualOverrideCommand command,
															   StationId stationId,
															   LaneId laneId,
															   VehicleClass vehicleClass,
															   OverrideReason reason,
															   MoneyAmount tariffAmount) {
		MoneyAmount amount = MoneyAmount.of(command.amount(), "COP");

		return Mono.just(ManualOverride.register(
				stationId,
				laneId,
				command.licensePlate(),
				vehicleClass,
				reason,
				command.operatorId(),
				amount,
				command.lprPhotoUrl()
			))
			.flatMap(override -> overrideRepository.save(override)
				.flatMap(saved -> publishEvents(override).thenReturn(saved))
				.doOnNext(saved -> log.info("Manual override registered: overrideId={}", saved.overrideId()))
				.map(ManualOverrideResponse::fromManualOverride)
				.doOnError(ex -> log.error("Error registering manual override", ex)));
	}

	private Mono<Void> publishEvents(ManualOverride override) {
		List<Object> events = override.pullDomainEvents();
		return reactor.core.publisher.Flux.fromIterable(events)
			.flatMap(event -> eventPublisher.publishToIncidents(event))
			.then();
	}

}
