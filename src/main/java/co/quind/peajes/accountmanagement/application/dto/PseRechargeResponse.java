package co.quind.peajes.accountmanagement.application.dto;

import co.quind.peajes.accountmanagement.domain.model.RechargeTransaction;

/**
 * Resultado del procesamiento de una notificación de pago PSE.
 *
 * @param transactionId       UUID de la transacción de recarga
 * @param accountId           UUID de la cuenta prepago
 * @param externalReferenceId referencia PSE de PayU
 * @param amount              monto procesado
 * @param currency            moneda (COP)
 * @param status              estado final: COMPLETED o FAILED
 * @param failureReason       razón del fallo si aplica
 * @param createdAt           timestamp ISO 8601 de la transacción
 * @param fromCache           true si fue devuelta por idempotencia
 */
public record PseRechargeResponse(
		String transactionId,
		String accountId,
		String externalReferenceId,
		String amount,
		String currency,
		String status,
		String failureReason,
		String createdAt,
		boolean fromCache
) {
	public static PseRechargeResponse from(RechargeTransaction tx, boolean fromCache) {
		return new PseRechargeResponse(
			tx.id().toString(),
			tx.accountId().toString(),
			tx.externalReferenceId().value(),
			tx.amount().toPlainString(),
			tx.amount().currency(),
			tx.status().name(),
			tx.failureReason(),
			tx.createdAt().toString(),
			fromCache
		);
	}
}
