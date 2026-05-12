package co.quind.peajes.tollcollection.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PassIdTest {

	@Test
	void of_tagIdAndInstant_createsCorrectFormat() {
		TagId tagId = TagId.of("A1B2C3D4");
		Instant instant = Instant.parse("2025-04-24T14:23:05Z");

		PassId passId = PassId.of(tagId, instant);

		assertEquals("A1B2C3D4-20250424T142305Z", passId.value());
	}

	@Test
	void constructor_invalidFormat_throwsIllegalArgument() {
		assertThrows(IllegalArgumentException.class,
			() -> new PassId("INVALID_FORMAT"));
	}

	@Test
	void constructor_validFormat_createsSuccessfully() {
		PassId passId = new PassId("A1B2C3D4-20250424T142305Z");
		assertEquals("A1B2C3D4-20250424T142305Z", passId.value());
	}

}
