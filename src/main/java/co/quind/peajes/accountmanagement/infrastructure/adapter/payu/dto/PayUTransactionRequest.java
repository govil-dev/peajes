package co.quind.peajes.accountmanagement.infrastructure.adapter.payu.dto;

public record PayUTransactionRequest(
		String accountId,
		String amount,
		String currency,
		String description
) {}
