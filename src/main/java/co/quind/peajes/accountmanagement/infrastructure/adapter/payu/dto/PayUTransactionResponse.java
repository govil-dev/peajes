package co.quind.peajes.accountmanagement.infrastructure.adapter.payu.dto;

public record PayUTransactionResponse(
		String externalReferenceId,
		String redirectUrl,
		String status
) {}
