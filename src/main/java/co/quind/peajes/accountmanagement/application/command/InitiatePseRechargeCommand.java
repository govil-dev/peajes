package co.quind.peajes.accountmanagement.application.command;

/**
 * Comando para iniciar una recarga de saldo vía PSE.
 *
 * @param accountId UUID de la cuenta prepago
 * @param amount    monto a recargar
 * @param currency  moneda (COP)
 */
public record InitiatePseRechargeCommand(
		String accountId,
		String amount,
		String currency
) {}
