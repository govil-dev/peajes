package co.quind.peajes.tollcollection.infrastructure.persistence;

import co.quind.peajes.tollcollection.domain.model.ManualOverride;
import co.quind.peajes.tollcollection.domain.model.OverrideReason;
import co.quind.peajes.tollcollection.domain.model.VehicleClass;
import co.quind.peajes.tollcollection.domain.port.out.ManualOverrideRepository;
import co.quind.peajes.tollcollection.domain.valueobject.LaneId;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.OverrideId;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ManualOverridePersistenceAdapter implements ManualOverrideRepository {

	private final ManualOverrideR2dbcRepository repository;

	@Override
	public Mono<ManualOverride> save(ManualOverride override) {
		ManualOverrideEntity entity = new ManualOverrideEntity(
			UUID.randomUUID(),
			override.overrideId().value(),
			override.stationId().toString(),
			override.laneId().toString(),
			override.licensePlate(),
			override.vehicleClass().name(),
			override.reason().name(),
			override.operatorId(),
			override.amount().toJsonString(),
			override.lprPhotoUrl(),
			override.registeredAt(),
			override.requiresAdminApproval()
		);
		return repository.save(entity)
			.map(saved -> reconstructManualOverride(saved));
	}

	@Override
	public Mono<ManualOverride> findByOverrideId(OverrideId overrideId) {
		return repository.findByOverrideId(overrideId.value())
			.map(this::reconstructManualOverride);
	}

	private ManualOverride reconstructManualOverride(ManualOverrideEntity entity) {
		return ManualOverride.fromPersisted(
			OverrideId.of(entity.overrideId()),
			StationId.of(entity.stationId()),
			LaneId.of(entity.laneId()),
			entity.licensePlate(),
			VehicleClass.valueOf(entity.vehicleClass()),
			OverrideReason.valueOf(entity.reason()),
			entity.operatorId(),
			MoneyAmount.of(entity.amount(), "COP"),
			entity.lprPhotoUrl(),
			entity.registeredAt(),
			entity.requiresAdminApproval()
		);
	}

}
