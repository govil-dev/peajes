package co.quind.peajes.tollcollection.domain.valueobject;

import java.util.Objects;

/**
 * Identificador del tag RFID del vehículo. Se considera PII — usar toMasked() en logs.
 */
public record TagId(String value) {

    public TagId {
        Objects.requireNonNull(value, "tagId value es requerido");
        if (value.isBlank()) {
            throw new IllegalArgumentException("tagId no puede ser vacío");
        }
    }

    /** Devuelve versión enmascarada segura para logs: ****XXXX (últimos 4 caracteres). */
    public String toMasked() {
        if (value.length() <= 4) return "****";
        return "****" + value.substring(value.length() - 4);
    }
}
