package co.quind.peajes.tollcollection.domain.port.out;

import co.quind.peajes.tollcollection.domain.model.Lane;
import co.quind.peajes.tollcollection.domain.valueobject.LaneId;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import reactor.core.publisher.Mono;

/** Puerto de salida para consulta del estado de carriles. */
public interface LaneRepository {
    Mono<Lane> findByLaneIdAndStationId(LaneId laneId, StationId stationId);
}
