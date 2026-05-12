package co.quind.peajes.tollcollection.domain.event;

public record TransactionAuthorized(
	String eventId,
	String passId,
	String accountId,
	String tariffAmount,
	String currency,
	String balanceAfter,
	String authorizedAt,
	String occurredAt,
	String stationId,
	String vehicleClass
) {}
