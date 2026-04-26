package co.quind.peajes.tollcollection.infrastructure.adapter.account.dto;

/** DTO de solicitud de débito a account-management-api. */
public record DeductionRequest(String passId, String amount, String currency) {}
