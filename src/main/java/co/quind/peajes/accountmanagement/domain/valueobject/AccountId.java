package co.quind.peajes.accountmanagement.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record AccountId(UUID value) {

	public AccountId {
		Objects.requireNonNull(value, "accountId must not be null");
	}

	public static AccountId of(String value) {
		return new AccountId(UUID.fromString(value));
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
