package co.quind.peajes.tollcollection.domain.event;

public record TransactionDeclined(
	String eventId,
	String passId,
	String tagId,
	String reason,
	String declinedAt,
	String occurredAt
) {}
