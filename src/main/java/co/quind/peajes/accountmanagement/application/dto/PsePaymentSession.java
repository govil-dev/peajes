package co.quind.peajes.accountmanagement.application.dto;

/**
 * Sesión de pago PSE creada por PayU al iniciar una recarga.
 *
 * @param externalReferenceId referencia de transacción PSE asignada por PayU
 * @param redirectUrl         URL de la pasarela PayU para redirigir al usuario
 */
public record PsePaymentSession(
		String externalReferenceId,
		String redirectUrl
) {}
