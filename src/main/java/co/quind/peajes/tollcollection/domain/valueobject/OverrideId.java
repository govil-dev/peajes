package co.quind.peajes.tollcollection.domain.valueobject;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public record OverrideId(String value) {

	private static final Pattern VALID_UUID = Pattern.compile(
		"^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
	);

	public OverrideId {
		Objects.requireNonNull(value, "overrideId must not be null");
		if (!VALID_UUID.matcher(value).matches()) {
			throw new IllegalArgumentException("overrideId must be a valid UUID");
		}
	}

	public static OverrideId random() {
		return new OverrideId(UUID.randomUUID().toString());
	}

	public static OverrideId of(String value) {
		return new OverrideId(value);
	}

	@Override
	public String toString() {
		return value;
	}

}
