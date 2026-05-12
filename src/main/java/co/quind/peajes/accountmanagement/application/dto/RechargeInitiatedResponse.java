package co.quind.peajes.accountmanagement.application.dto;

/**
 * Respuesta al UserCarrier al iniciar una recarga PSE.
 *
 * @param accountId           UUID de la cuenta prepago
 * @param externalReferenceId referencia PSE asignada por PayU
 * @param redirectUrl         URL de redirección a la pasarela de pago
 */
public record RechargeInitiatedResponse(
		String accountId,
		String externalReferenceId,
		String redirectUrl
) {}
