package co.quind.peajes.tollcollection.domain.valueobject;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

public record PassId(String value) {

	private static final Pattern PASS_ID_PATTERN =
		Pattern.compile("^[A-F0-9]{8}-\\d{8}T\\d{6}Z$");

	public PassId {
		Objects.requireNonNull(value, "value must not be null");
		if (!PASS_ID_PATTERN.matcher(value).matches()) {
			throw new IllegalArgumentException(
				"passId must match format {tagHex(8)}-{yyyyMMdd'T'HHmmss'Z'}: " + value);
		}
	}

	public static PassId of(TagId tagId, Instant detectedAt) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
			.withZone(ZoneOffset.UTC);
		String timestamp = formatter.format(detectedAt);
		return new PassId(tagId.value() + "-" + timestamp);
	}

	@Override
	public String toString() {
		return value;
	}

}
