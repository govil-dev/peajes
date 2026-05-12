package co.quind.peajes.tollcollection.infrastructure.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("manual_overrides")
public record ManualOverrideEntity(
	@Id
	UUID id,

	@Column("override_id")
	String overrideId,

	@Column("station_id")
	String stationId,

	@Column("lane_id")
	String laneId,

	@Column("license_plate")
	String licensePlate,

	@Column("vehicle_class")
	String vehicleClass,

	@Column("reason")
	String reason,

	@Column("operator_id")
	String operatorId,

	@Column("amount")
	String amount,

	@Column("lpr_photo_url")
	String lprPhotoUrl,

	@Column("registered_at")
	Instant registeredAt,

	@Column("requires_admin_approval")
	boolean requiresAdminApproval
) {}
