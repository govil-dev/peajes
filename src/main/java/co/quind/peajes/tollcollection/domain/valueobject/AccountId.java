package co.quind.peajes.tollcollection.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record AccountId(UUID value) {

	public AccountId {
		Objects.requireNonNull(value, "value must not be null");
	}

	public static AccountId of(String uuidString) {
		return new AccountId(UUID.fromString(uuidString));
	}

	public static AccountId random() {
		return new AccountId(UUID.randomUUID());
	}

	@Override
	public String toString() {
		return value.toString();
	}

}
