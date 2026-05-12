package co.quind.peajes.tollcollection.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record LaneId(UUID value) {

	public LaneId {
		Objects.requireNonNull(value, "value must not be null");
	}

	public static LaneId of(String uuidString) {
		return new LaneId(UUID.fromString(uuidString));
	}

	public static LaneId random() {
		return new LaneId(UUID.randomUUID());
	}

	@Override
	public String toString() {
		return value.toString();
	}

}
