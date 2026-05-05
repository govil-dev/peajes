package co.quind.peajes.tollcollection.infrastructure.persistence;

import co.quind.peajes.tollcollection.domain.model.*;
import co.quind.peajes.tollcollection.domain.port.out.TollPassRepository;
import co.quind.peajes.tollcollection.domain.valueobject.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class TollPassRepositoryAdapter implements TollPassRepository {

	private final TollPassR2dbcRepository r2dbcRepository;

	@Override
	public Mono<TollPass> findByPassId(PassId passId) {
		return r2dbcRepository.findByPassId(passId.value())
			.map(this::toDomain);
	}

	@Override
	public Mono<TollPass> save(TollPass tollPass) {
		TollPassEntity entity = toEntity(tollPass);
		return r2dbcRepository.save(entity)
			.map(this::toDomain);
	}

	private TollPass toDomain(TollPassEntity entity) {
		PassId passId = new PassId(entity.passId());
		TagId tagId = TagId.of(entity.tagId());
		StationId stationId = new StationId(entity.stationId());
		LaneId laneId = new LaneId(entity.laneId());
		VehicleClass vehicleClass = entity.vehicleClass() != null
			? VehicleClass.valueOf(entity.vehicleClass())
			: null;
		MoneyAmount tariff = entity.tariffAmount() != null
			? new MoneyAmount(entity.tariffAmount(), entity.currency())
			: null;
		TollPassStatus status = TollPassStatus.valueOf(entity.status());
		DeclineReason declineReason = entity.declineReason() != null
			? DeclineReason.valueOf(entity.declineReason())
			: null;

		if (status == TollPassStatus.AUTHORIZED) {
			return TollPass.authorize(passId, tagId, stationId, laneId, vehicleClass, tariff, entity.detectedAt());
		} else {
			return TollPass.decline(passId, tagId, stationId, laneId, declineReason, entity.detectedAt());
		}
	}

	private TollPassEntity toEntity(TollPass tollPass) {
		return new TollPassEntity(
			tollPass.id(),
			tollPass.passId().value(),
			tollPass.tagId().value(),
			tollPass.stationId().value(),
			tollPass.laneId().value(),
			tollPass.vehicleClass() != null ? tollPass.vehicleClass().name() : null,
			tollPass.tariff() != null ? tollPass.tariff().amount() : null,
			tollPass.tariff() != null ? tollPass.tariff().currency() : "COP",
			tollPass.status().name(),
			tollPass.declineReason() != null ? tollPass.declineReason().name() : null,
			tollPass.detectedAt(),
			Instant.now()
		);
	}

}
