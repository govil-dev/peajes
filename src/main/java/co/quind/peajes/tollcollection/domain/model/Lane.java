package co.quind.peajes.tollcollection.domain.model;

import co.quind.peajes.tollcollection.domain.valueobject.LaneId;
import co.quind.peajes.tollcollection.domain.valueobject.StationId;
import java.util.UUID;

public record Lane(UUID id, StationId stationId, String laneCode, LaneStatus status) {

	public boolean isOpen() {
		return status == LaneStatus.OPEN;
	}

}
