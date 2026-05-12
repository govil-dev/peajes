package co.quind.peajes.accountmanagement.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Saldo monetario de una cuenta prepago. Siempre en COP con escala 2.
 */
public record Balance(BigDecimal amount, String currency) {

	public Balance {
		Objects.requireNonNull(amount, "balance amount must not be null");
		Objects.requireNonNull(currency, "balance currency must not be null");
		if (amount.signum() < 0) {
			throw new IllegalArgumentException("balance amount must not be negative");
		}
		if (amount.scale() > 2) {
			throw new IllegalArgumentException("balance amount scale must not exceed 2");
		}
		if (!"COP".equals(currency)) {
			throw new IllegalArgumentException("balance currency must be COP");
		}
	}

	public static Balance of(BigDecimal amount) {
		return new Balance(amount.setScale(2, RoundingMode.HALF_UP), "COP");
	}

	public static Balance of(String amount, String currency) {
		return new Balance(new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP), currency);
	}

	public static Balance zero() {
		return new Balance(BigDecimal.ZERO.setScale(2), "COP");
	}

	public Balance add(Balance other) {
		if (!this.currency.equals(other.currency)) {
			throw new IllegalArgumentException("cannot add balances of different currencies");
		}
		return new Balance(this.amount.add(other.amount), this.currency);
	}

	public String toPlainString() {
		return amount.toPlainString();
	}

	public String toDisplayString() {
		return String.format("%s %s", amount.toPlainString(), currency);
	}
}
