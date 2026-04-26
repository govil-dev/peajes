package co.quind.peajes.tollcollection.api.dto;

import java.time.Instant;

/**
 * Solicitud de procesamiento de paso vehicular recibida desde la antena RFID.
 */
public record TollPassRequest(
        String tagId,
        String stationId,
        String laneId,
        String vehicleClass,
        Instant detectedAt
) {}
