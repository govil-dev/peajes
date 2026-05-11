package co.quind.peajes.tollcollection.domain.event;

public record ManualOverrideRegistered(
	String eventId,
	String overrideId,
	String stationId,
	String laneId,
	String licensePlate,
	String vehicleClass,
	String reason,
	String operatorId,
	String amount,
	String currency,
	String registeredAt
) {}
