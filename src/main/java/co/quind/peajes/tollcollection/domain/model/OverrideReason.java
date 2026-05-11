package co.quind.peajes.tollcollection.domain.model;

public enum OverrideReason {
	TAG_DAMAGED,
	ANTENNA_MALFUNCTION,
	VEHICLE_UNREADABLE,
	MANUAL_GRANT;

	public static OverrideReason fromString(String value) {
		try {
			return OverrideReason.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid override reason: " + value);
		}
	}

}
