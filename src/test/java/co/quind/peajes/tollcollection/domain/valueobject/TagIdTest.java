package co.quind.peajes.tollcollection.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagIdTest {

	@Test
	void of_validHexString_createsSuccessfully() {
		TagId tagId = TagId.of("A1B2C3D4");
		assertEquals("A1B2C3D4", tagId.value());
	}

	@Test
	void of_lowerCaseHex_convertsToUpperCase() {
		TagId tagId = TagId.of("a1b2c3d4");
		assertEquals("A1B2C3D4", tagId.value());
	}

	@Test
	void of_invalidLength_throwsIllegalArgument() {
		assertThrows(IllegalArgumentException.class, () -> TagId.of("A1B2C3"));
	}

	@Test
	void toMasked_returnsMaskedValue() {
		TagId tagId = TagId.of("A1B2C3D4");
		assertEquals("****C3D4", tagId.toMasked());
	}

}
