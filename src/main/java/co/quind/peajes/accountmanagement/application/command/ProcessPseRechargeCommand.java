package co.quind.peajes.accountmanagement.application.command;

/**
 * Comando para procesar la notificación de resultado de pago PSE recibida desde PayU.
 *
 * @param accountId           UUID de la cuenta prepago a acreditar
 * @param externalReferenceId referencia de transacción PSE de PayU (clave de idempotencia)
 * @param amount              monto de la recarga
 * @param currency            moneda (COP)
 * @param paymentStatus       resultado del pago: COMPLETED o FAILED
 * @param failureReason       razón del fallo (ej. INSUFFICIENT_FUNDS_BANK), nullable si exitoso
 */
public record ProcessPseRechargeCommand(
		String accountId,
		String externalReferenceId,
		String amount,
		String currency,
		String paymentStatus,
		String failureReason
) {}
