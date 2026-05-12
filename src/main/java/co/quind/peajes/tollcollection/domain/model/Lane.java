package co.quind.peajes.tollcollection.domain.model;

import co.quind.peajes.tollcollection.domain.valueobject.LaneId;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;

public record Lane(LaneId id, StationId stationId, LaneStatus status) {

	public boolean isOpen() {
		return status == LaneStatus.OPEN;
	}

}
