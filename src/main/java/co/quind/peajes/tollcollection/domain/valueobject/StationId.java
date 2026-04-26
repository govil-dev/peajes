package co.quind.peajes.tollcollection.domain.valueobject;

import java.util.Objects;

/** Identificador de la estación de peaje. */
public record StationId(String value) {
    public StationId {
        Objects.requireNonNull(value, "stationId value es requerido");
        if (value.isBlank()) throw new IllegalArgumentException("stationId no puede ser vacío");
    }
}
