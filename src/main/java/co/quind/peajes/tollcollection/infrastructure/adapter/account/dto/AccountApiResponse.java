package co.quind.peajes.tollcollection.infrastructure.adapter.account.dto;

/** DTO de respuesta de account-management-api para datos de cuenta y tag. */
public record AccountApiResponse(
        String accountId,
        String tagId,
        String tagStatus,
        String balance,
        String currency
) {}
