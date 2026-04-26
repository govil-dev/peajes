package co.quind.peajes.tollcollection.domain.event;

import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.PassId;
import co.quind.peajes.tollcollection.domain.valueobject.TagId;

import java.time.Instant;

/**
 * Evento publicado en toll.transactions.v1 cuando un cobro de peaje es autorizado.
 */
public record TransactionAuthorized(
        String passId,
        String tagId,
        String amount,
        String currency,
        String balanceAfter,
        String authorizedAt,
        String eventType
) {
    public static TransactionAuthorized from(PassId passId, TagId tagId,
                                              MoneyAmount amount, MoneyAmount balanceAfter,
                                              Instant authorizedAt) {
        return new TransactionAuthorized(
                passId.value(), tagId.value(),
                amount.toDecimalString(), amount.currency(),
                balanceAfter.toDecimalString(),
                authorizedAt.toString(), "TransactionAuthorized"
        );
    }
}
