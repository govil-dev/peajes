package co.quind.peajes.tollcollection.infrastructure.persistence;

import co.quind.peajes.tollcollection.domain.model.Lane;
import co.quind.peajes.tollcollection.domain.model.LaneStatus;
import co.quind.peajes.tollcollection.domain.port.out.LaneRepository;
import co.quind.peajes.tollcollection.domain.valueobject.LaneId;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LaneRepositoryAdapter implements LaneRepository {

	private final LaneR2dbcRepository r2dbcRepository;

	@Override
	public Mono<Lane> findByLaneId(LaneId laneId) {
		return r2dbcRepository.findById(laneId.value())
			.map(this::toDomain);
	}

	private Lane toDomain(LaneEntity entity) {
		return new Lane(
			entity.id(),
			new StationId(entity.stationId()),
			entity.laneCode(),
			LaneStatus.valueOf(entity.status())
		);
	}

}
