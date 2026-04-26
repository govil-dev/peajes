package co.quind.peajes.tollcollection.domain.model;

import co.quind.peajes.tollcollection.domain.valueobject.*;

import java.time.Instant;

/**
 * Agregado raíz que representa un paso vehicular por una estación de peaje.
 * Una vez creado su estado no cambia (inmutable en dominio).
 */
public record TollPass(
        PassId passId,
        TagId tagId,
        StationId stationId,
        LaneId laneId,
        VehicleClass vehicleClass,
        MoneyAmount amount,
        TransactionStatus status,
        DeclineReason declineReason,
        Instant detectedAt,
        Instant processedAt
) {
    public static TollPass authorized(PassId passId, TagId tagId, StationId stationId,
                                      LaneId laneId, VehicleClass vehicleClass,
                                      MoneyAmount amount, Instant detectedAt) {
        return new TollPass(passId, tagId, stationId, laneId, vehicleClass,
                amount, TransactionStatus.AUTHORIZED, null, detectedAt, Instant.now());
    }

    public static TollPass declined(PassId passId, TagId tagId, StationId stationId,
                                    LaneId laneId, VehicleClass vehicleClass,
                                    MoneyAmount amount, DeclineReason reason, Instant detectedAt) {
        return new TollPass(passId, tagId, stationId, laneId, vehicleClass,
                amount, TransactionStatus.DECLINED, reason, detectedAt, Instant.now());
    }
}
