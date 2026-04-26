package co.quind.peajes.tollcollection.domain.valueobject;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Identificador único de un paso vehicular. Formato: {tagId}-{yyyyMMdd'T'HHmmss'Z'}.
 * Ejemplo: A1B2C3D4-20250424T142305Z
 */
public record PassId(String value) {

    private static final DateTimeFormatter TS_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'").withZone(ZoneOffset.UTC);

    public PassId {
        Objects.requireNonNull(value, "passId value es requerido");
        if (value.isBlank()) {
            throw new IllegalArgumentException("passId no puede ser vacío");
        }
    }

    /** Construye el passId canónico a partir del tagId y el instante de detección. */
    public static PassId of(TagId tagId, Instant detectedAt) {
        Objects.requireNonNull(tagId, "tagId es requerido");
        Objects.requireNonNull(detectedAt, "detectedAt es requerido");
        return new PassId(tagId.value() + "-" + TS_FORMAT.format(detectedAt));
    }
}
