package co.quind.peajes.tollcollection.api.dto;

import co.quind.peajes.tollcollection.application.dto.TollPassResult;

/**
 * Respuesta HTTP del procesamiento de un paso vehicular.
 */
public record TollPassResponse(
        String passId,
        String status,
        String amount,
        String currency,
        String declineReason,
        String processedAt,
        boolean fromCache
) {
    public static TollPassResponse from(TollPassResult result) {
        return new TollPassResponse(
                result.passId(),
                result.status().name(),
                result.amount(),
                result.currency(),
                result.declineReason() != null ? result.declineReason().name() : null,
                result.processedAt(),
                result.fromCache()
        );
    }
}
