package co.quind.peajes.tollcollection.infrastructure.adapter.account.dto;

/** DTO de respuesta de débito de account-management-api. */
public record DeductionApiResponse(String newBalance, String currency) {}
