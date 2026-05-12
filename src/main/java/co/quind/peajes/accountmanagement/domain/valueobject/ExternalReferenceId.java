package co.quind.peajes.accountmanagement.domain.valueobject;

import java.util.Objects;

/**
 * Referencia de transacción PSE provista por PayU. Clave de idempotencia para recargas.
 */
public record ExternalReferenceId(String value) {

	public ExternalReferenceId {
		Objects.requireNonNull(value, "externalReferenceId must not be null");
		if (value.isBlank()) {
			throw new IllegalArgumentException("externalReferenceId must not be blank");
		}
	}

	/** Enmascara el valor para logs: muestra solo los últimos 4 caracteres. */
	public String masked() {
		if (value.length() <= 4) return "****";
		return "****" + value.substring(value.length() - 4);
	}

	@Override
	public String toString() {
		return masked();
	}
}
