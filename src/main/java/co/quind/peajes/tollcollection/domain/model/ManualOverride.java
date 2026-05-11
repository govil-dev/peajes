package co.quind.peajes.tollcollection.domain.model;

import co.quind.peajes.tollcollection.domain.event.ManualOverrideRegistered;
import co.quind.peajes.tollcollection.domain.valueobject.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ManualOverride {
	private final OverrideId overrideId;
	private final StationId stationId;
	private final LaneId laneId;
	private final String licensePlate;
	private final VehicleClass vehicleClass;
	private final OverrideReason reason;
	private final String operatorId;
	private final MoneyAmount amount;
	private final String lprPhotoUrl;
	private final Instant registeredAt;
	private final boolean requiresAdminApproval;
	private final List<Object> domainEvents = new ArrayList<>();

	private ManualOverride(OverrideId overrideId, StationId stationId, LaneId laneId,
						   String licensePlate, VehicleClass vehicleClass, OverrideReason reason,
						   String operatorId, MoneyAmount amount, String lprPhotoUrl,
						   Instant registeredAt, boolean requiresAdminApproval) {
		this.overrideId = overrideId;
		this.stationId = stationId;
		this.laneId = laneId;
		this.licensePlate = licensePlate;
		this.vehicleClass = vehicleClass;
		this.reason = reason;
		this.operatorId = operatorId;
		this.amount = amount;
		this.lprPhotoUrl = lprPhotoUrl;
		this.registeredAt = registeredAt;
		this.requiresAdminApproval = requiresAdminApproval;
	}

	public static ManualOverride register(StationId stationId, LaneId laneId,
										   String licensePlate, VehicleClass vehicleClass,
										   OverrideReason reason, String operatorId,
										   MoneyAmount amount, String lprPhotoUrl) {
		OverrideId overrideId = OverrideId.random();
		boolean needsApproval = reason == OverrideReason.MANUAL_GRANT;
		ManualOverride override = new ManualOverride(overrideId, stationId, laneId,
			licensePlate, vehicleClass, reason, operatorId, amount, lprPhotoUrl,
			Instant.now(), needsApproval);
		override.raiseManualOverrideRegistered();
		return override;
	}

	public static ManualOverride fromPersisted(OverrideId overrideId, StationId stationId,
											   LaneId laneId, String licensePlate,
											   VehicleClass vehicleClass, OverrideReason reason,
											   String operatorId, MoneyAmount amount,
											   String lprPhotoUrl, Instant registeredAt,
											   boolean requiresAdminApproval) {
		return new ManualOverride(overrideId, stationId, laneId, licensePlate, vehicleClass,
			reason, operatorId, amount, lprPhotoUrl, registeredAt, requiresAdminApproval);
	}

	private void raiseManualOverrideRegistered() {
		domainEvents.add(new ManualOverrideRegistered(
			UUID.randomUUID().toString(),
			overrideId.value(),
			stationId.toString(),
			laneId.toString(),
			maskLicensePlate(licensePlate),
			vehicleClass.name(),
			reason.name(),
			operatorId,
			amount.toJsonString(),
			amount.currency(),
			registeredAt.toString()
		));
	}

	private static String maskLicensePlate(String licensePlate) {
		if (licensePlate == null || licensePlate.length() < 2) {
			return "****";
		}
		return licensePlate.substring(0, 1) + "***" + licensePlate.substring(licensePlate.length() - 1);
	}

	public List<Object> pullDomainEvents() {
		List<Object> events = new ArrayList<>(domainEvents);
		domainEvents.clear();
		return events;
	}

	public OverrideId overrideId() { return overrideId; }
	public StationId stationId() { return stationId; }
	public LaneId laneId() { return laneId; }
	public String licensePlate() { return licensePlate; }
	public VehicleClass vehicleClass() { return vehicleClass; }
	public OverrideReason reason() { return reason; }
	public String operatorId() { return operatorId; }
	public MoneyAmount amount() { return amount; }
	public String lprPhotoUrl() { return lprPhotoUrl; }
	public Instant registeredAt() { return registeredAt; }
	public boolean requiresAdminApproval() { return requiresAdminApproval; }

}
