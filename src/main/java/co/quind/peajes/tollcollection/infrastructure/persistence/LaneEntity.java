package co.quind.peajes.tollcollection.infrastructure.persistence;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("tc_lanes")
public record LaneEntity(
	@Id UUID id,
	@Column("station_id") UUID stationId,
	@Column("lane_code") String laneCode,
	@Column("status") String status
) {}
