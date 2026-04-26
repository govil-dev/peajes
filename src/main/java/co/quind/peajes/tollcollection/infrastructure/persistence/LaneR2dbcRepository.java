package co.quind.peajes.tollcollection.infrastructure.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LaneR2dbcRepository extends ReactiveCrudRepository<LaneEntity, String> {
    Mono<LaneEntity> findByLaneIdAndStationId(String laneId, String stationId);
}
