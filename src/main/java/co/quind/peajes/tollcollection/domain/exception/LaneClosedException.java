package co.quind.peajes.tollcollection.domain.exception;

import co.quind.peajes.tollcollection.domain.valueobject.LaneId;

/** Se lanza cuando el carril no está en estado OPEN. */
public class LaneClosedException extends RuntimeException {
    public LaneClosedException(LaneId laneId) {
        super("Carril no disponible: " + laneId.value());
    }
}
