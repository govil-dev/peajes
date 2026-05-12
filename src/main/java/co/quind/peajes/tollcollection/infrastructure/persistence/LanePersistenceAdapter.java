package co.quind.peajes.tollcollection.infrastructure.persistence;

import co.quind.peajes.tollcollection.domain.model.Lane;
import co.quind.peajes.tollcollection.domain.model.LaneStatus;
import co.quind.peajes.tollcollection.domain.port.out.LaneRepository;
import co.quind.peajes.tollcollection.domain.valueobject.LaneId;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LanePersistenceAdapter implements LaneRepository {

    private final LaneR2dbcRepository r2dbcRepository;

    public LanePersistenceAdapter(LaneR2dbcRepository r2dbcRepository) {
        this.r2dbcRepository = r2dbcRepository;
    }

    @Override
    public Mono<Lane> findByLaneIdAndStationId(LaneId laneId, StationId stationId) {
        return r2dbcRepository.findByIdAndStationId(laneId.value(), stationId.value())
                .map(e -> new Lane(new LaneId(e.id()), new StationId(e.stationId()),
                        LaneStatus.valueOf(e.status())));
    }
}
