package co.quind.peajes.tollcollection.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

public record TagId(String value) {

	private static final Pattern TAG_ID_PATTERN = Pattern.compile("^[A-F0-9]{8}$");

	public TagId {
		Objects.requireNonNull(value, "value must not be null");
		String upperValue = value.toUpperCase();
		if (!TAG_ID_PATTERN.matcher(upperValue).matches()) {
			throw new IllegalArgumentException(
				"tagId must be 8 hex characters (A-F, 0-9): " + value);
		}
	}

	public static TagId of(String value) {
		return new TagId(value.toUpperCase());
	}

	public String toMasked() {
		String upper = value.toUpperCase();
		return "****" + upper.substring(4);
	}

	@Override
	public String toString() {
		return value.toUpperCase();
	}

}
