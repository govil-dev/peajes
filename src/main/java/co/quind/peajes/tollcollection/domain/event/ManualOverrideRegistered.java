package co.quind.peajes.tollcollection.domain.event;

import java.time.Instant;

public record ManualOverrideRegistered(
        String overrideId,
        String stationId,
        String laneId,
        String licensePlateMasked,
        String vehicleClass,
        String reason,
        String operatorId,
        String amount,
        String currency,
        String registeredAt,
        String eventType
) {
    public static ManualOverrideRegistered of(
            String overrideId,
            String stationId,
            String laneId,
            String licensePlate,
            String vehicleClass,
            String reason,
            String operatorId,
            String amount,
            String currency,
            Instant registeredAt
    ) {
        return new ManualOverrideRegistered(
                overrideId,
                stationId,
                laneId,
                maskPlate(licensePlate),
                vehicleClass,
                reason,
                operatorId,
                amount,
                currency,
                registeredAt.toString(),
                "ManualOverrideRegistered"
        );
    }

    private static String maskPlate(String plate) {
        if (plate == null || plate.length() < 3) {
            return "***";
        }
        return plate.substring(0, 3) + "-***";
    }
}
