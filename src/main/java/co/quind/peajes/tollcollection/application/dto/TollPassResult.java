package co.quind.peajes.tollcollection.application.dto;

import co.quind.peajes.tollcollection.domain.model.DeclineReason;
import co.quind.peajes.tollcollection.domain.model.TollPass;
import co.quind.peajes.tollcollection.domain.model.TransactionStatus;

/**
 * Resultado del procesamiento de un paso vehicular. Puede ser resultado en caché (idempotente)
 * o resultado nuevo.
 */
public record TollPassResult(
        String passId,
        TransactionStatus status,
        String amount,
        String currency,
        DeclineReason declineReason,
        String processedAt,
        boolean fromCache
) {
    public static TollPassResult fromExisting(TollPass pass) {
        return new TollPassResult(
                pass.passId().value(), pass.status(),
                pass.amount().toDecimalString(), pass.amount().currency(),
                pass.declineReason(), pass.processedAt().toString(), true
        );
    }

    public static TollPassResult authorized(TollPass pass) {
        return new TollPassResult(
                pass.passId().value(), TransactionStatus.AUTHORIZED,
                pass.amount().toDecimalString(), pass.amount().currency(),
                null, pass.processedAt().toString(), false
        );
    }

    public static TollPassResult declined(TollPass pass) {
        return new TollPassResult(
                pass.passId().value(), TransactionStatus.DECLINED,
                pass.amount().toDecimalString(), pass.amount().currency(),
                pass.declineReason(), pass.processedAt().toString(), false
        );
    }
}
