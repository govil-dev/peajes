package co.quind.peajes.tollcollection.domain.event;

import co.quind.peajes.tollcollection.domain.model.DeclineReason;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.PassId;
import co.quind.peajes.tollcollection.domain.valueobject.TagId;

import java.time.Instant;

/**
 * Evento publicado en toll.transactions.v1 cuando un cobro de peaje es declinado.
 */
public record TransactionDeclined(
        String passId,
        String tagId,
        String amount,
        String currency,
        String reason,
        String declinedAt,
        String eventType
) {
    public static TransactionDeclined from(PassId passId, TagId tagId,
                                            MoneyAmount amount, DeclineReason reason,
                                            Instant declinedAt) {
        return new TransactionDeclined(
                passId.value(), tagId.value(),
                amount.toDecimalString(), amount.currency(),
                reason.name(), declinedAt.toString(), "TransactionDeclined"
        );
    }
}
