package co.quind.peajes.tollcollection.application.command;

import java.time.Instant;

/**
 * Comando para procesar un paso vehicular recibido desde la antena RFID.
 */
public record ProcessTollPassCommand(
        String tagId,
        String stationId,
        String laneId,
        String vehicleClass,
        Instant detectedAt
) {}
