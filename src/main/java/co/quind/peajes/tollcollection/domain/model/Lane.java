package co.quind.peajes.tollcollection.domain.model;

import co.quind.peajes.tollcollection.domain.valueobject.LaneId;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;

/** Carril de una estación de peaje. */
public record Lane(LaneId laneId, StationId stationId, LaneStatus status) {
    public boolean isOpen() {
        return status == LaneStatus.OPEN;
    }
}
