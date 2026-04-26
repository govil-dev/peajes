package co.quind.peajes.tollcollection.domain.event;

import co.quind.peajes.tollcollection.domain.model.VehicleClass;
import co.quind.peajes.tollcollection.domain.valueobject.*;

import java.time.Instant;

/**
 * Evento publicado cuando se registra un nuevo paso vehicular (autorizado o declinado).
 */
public record TollPassRegistered(
        String passId,
        String tagId,
        String stationId,
        String laneId,
        String vehicleClass,
        String amount,
        String currency,
        String detectedAt,
        String eventType
) {
    public static TollPassRegistered from(PassId passId, TagId tagId, StationId stationId,
                                          LaneId laneId, VehicleClass vehicleClass,
                                          MoneyAmount amount, Instant detectedAt) {
        return new TollPassRegistered(
                passId.value(), tagId.value(), stationId.value(), laneId.value(),
                vehicleClass.name(), amount.toDecimalString(), amount.currency(),
                detectedAt.toString(), "TollPassRegistered"
        );
    }
}
