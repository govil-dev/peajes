package co.quind.peajes.tollcollection.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record StationId(UUID value) {

	public StationId {
		Objects.requireNonNull(value, "value must not be null");
	}

	public static StationId of(String uuidString) {
		return new StationId(UUID.fromString(uuidString));
	}

	public static StationId random() {
		return new StationId(UUID.randomUUID());
	}

	@Override
	public String toString() {
		return value.toString();
	}

}
