package co.quind.peajes.tollcollection.domain.port.out;

import co.quind.peajes.tollcollection.domain.valueobject.LaneId;
import reactor.core.publisher.Mono;

/** Puerto de salida para control de barreras físicas vía WebSocket. */
public interface BarrierControlPort {
    Mono<Void> openBarrier(LaneId laneId);
    Mono<Void> notifyOperator(LaneId laneId, String message);
}
