package co.quind.peajes.accountmanagement.application.dto;

import jakarta.validation.constraints.NotBlank;

public record PayUNotificationRequest(
		@NotBlank String accountId,
		@NotBlank String externalReferenceId,
		@NotBlank String amount,
		@NotBlank String currency,
		@NotBlank String paymentStatus,
		String failureReason
) {}
