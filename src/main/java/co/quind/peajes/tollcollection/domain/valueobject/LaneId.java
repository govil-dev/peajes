package co.quind.peajes.tollcollection.domain.valueobject;

import java.util.Objects;

/** Identificador del carril de peaje. */
public record LaneId(String value) {
    public LaneId {
        Objects.requireNonNull(value, "laneId value es requerido");
        if (value.isBlank()) throw new IllegalArgumentException("laneId no puede ser vacío");
    }
}
