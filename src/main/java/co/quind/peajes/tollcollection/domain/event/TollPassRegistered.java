package co.quind.peajes.tollcollection.domain.event;

public record TollPassRegistered(
	String eventId,
	String passId,
	String tagId,
	String stationId,
	String laneId,
	String vehicleClass,
	String tariffAmount,
	String currency,
	String detectedAt,
	String occurredAt
) {}
