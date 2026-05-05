package co.quind.peajes.tollcollection.domain.event;

public record AccountBalanceLow(
	String eventId,
	String accountId,
	String currentBalance,
	String currency,
	String thresholdAmount,
	String occurredAt
) {}
