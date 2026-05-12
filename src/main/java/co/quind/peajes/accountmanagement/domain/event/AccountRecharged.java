package co.quind.peajes.accountmanagement.domain.event;

/**
 * Evento publicado en 'account.events.v1' cuando una recarga PSE es acreditada exitosamente.
 *
 * @param eventId              identificador único del evento (UUID)
 * @param accountId            cuenta prepago acreditada
 * @param rechargeTransactionId identificador de la transacción de recarga
 * @param rechargedAmount      monto acreditado
 * @param currency             moneda (COP)
 * @param balanceAfter         saldo total tras la recarga
 * @param source               origen del pago (PSE)
 * @param externalReferenceId  referencia de transacción PSE de PayU
 * @param occurredAt           timestamp ISO 8601 del evento
 */
public record AccountRecharged(
		String eventId,
		String accountId,
		String rechargeTransactionId,
		String rechargedAmount,
		String currency,
		String balanceAfter,
		String source,
		String externalReferenceId,
		String occurredAt
) {}
