package co.quind.peajes.tollcollection.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("tc_toll_passes")
public record TollPassEntity(
	@Id UUID id,
	@Column("pass_id") String passId,
	@Column("tag_id") String tagId,
	@Column("station_id") UUID stationId,
	@Column("lane_id") UUID laneId,
	@Column("vehicle_class") String vehicleClass,
	@Column("tariff_amount") BigDecimal tariffAmount,
	@Column("currency") String currency,
	@Column("status") String status,
	@Column("decline_reason") String declineReason,
	@Column("detected_at") Instant detectedAt,
	@Column("processed_at") Instant processedAt
) {}
