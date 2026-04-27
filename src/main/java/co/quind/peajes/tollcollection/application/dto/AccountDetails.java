package co.quind.peajes.tollcollection.application.dto;

import co.quind.peajes.tollcollection.domain.model.TagStatus;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.TagId;

/**
 * Datos de la cuenta y tag retornados por account-management-api.
 *
 * <p>Inmutable. Construido en el adapter HTTP a partir del response del downstream;
 * el dominio nunca debe construir este DTO directamente.
 */
public record AccountDetails(
        String accountId,
        TagId tagId,
        TagStatus tagStatus,
        MoneyAmount balance
) {}
