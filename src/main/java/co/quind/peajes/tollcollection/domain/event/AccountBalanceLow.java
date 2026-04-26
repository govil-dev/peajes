package co.quind.peajes.tollcollection.domain.event;

import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.TagId;

import java.time.Instant;

/**
 * Evento publicado en toll.accounts.v1 cuando el saldo cae por debajo del umbral de alerta.
 */
public record AccountBalanceLow(
        String tagId,
        String currentBalance,
        String currency,
        String alertThreshold,
        String occurredAt,
        String eventType
) {
    public static AccountBalanceLow from(TagId tagId, MoneyAmount currentBalance,
                                          MoneyAmount alertThreshold, Instant occurredAt) {
        return new AccountBalanceLow(
                tagId.value(),
                currentBalance.toDecimalString(), currentBalance.currency(),
                alertThreshold.toDecimalString(),
                occurredAt.toString(), "AccountBalanceLow"
        );
    }
}
