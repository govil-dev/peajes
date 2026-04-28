package co.quind.peajes.tollcollection.application.command;

import java.time.Instant;

public record RegisterManualOverrideCommand(
        String stationId,
        String laneId,
        String licensePlate,
        String vehicleClass,
        String reason,
        String operatorId,
        String lprPhotoUrl,
        Instant detectedAt
) {}
