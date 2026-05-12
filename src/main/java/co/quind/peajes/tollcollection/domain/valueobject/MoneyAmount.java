package co.quind.peajes.tollcollection.domain.valueobject;

import co.quind.peajes.tollcollection.domain.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.util.Objects;

public record MoneyAmount(BigDecimal amount, String currency) {

	public MoneyAmount {
		Objects.requireNonNull(amount, "amount must not be null");
		Objects.requireNonNull(currency, "currency must not be null");
		if (amount.signum() < 0) {
			throw new IllegalArgumentException("amount must not be negative");
		}
		if (amount.scale() > 2) {
			throw new IllegalArgumentException("amount scale must not exceed 2");
		}
		if (!currency.equals("COP")) {
			throw new IllegalArgumentException("currency must be COP");
		}
	}

	public static MoneyAmount of(BigDecimal amount) {
		return new MoneyAmount(amount.setScale(2, java.math.RoundingMode.HALF_UP), "COP");
	}

	public static MoneyAmount of(String amount, String currency) {
		return new MoneyAmount(new BigDecimal(amount).setScale(2, java.math.RoundingMode.HALF_UP), currency);
	}

	public String toDecimalString() {
		return toJsonString();
	}

	public static MoneyAmount zero() {
		return new MoneyAmount(BigDecimal.ZERO.setScale(2), "COP");
	}

	public MoneyAmount subtract(MoneyAmount other) {
		if (!this.currency.equals(other.currency)) {
			throw new IllegalArgumentException("cannot subtract different currencies");
		}
		BigDecimal result = this.amount.subtract(other.amount);
		if (result.signum() < 0) {
			throw new InsufficientBalanceException("insufficient balance: " + this.amount + " - " + other.amount);
		}
		return new MoneyAmount(result, this.currency);
	}

	public boolean isLessThan(MoneyAmount other) {
		if (!this.currency.equals(other.currency)) {
			throw new IllegalArgumentException("cannot compare different currencies");
		}
		return this.amount.compareTo(other.amount) < 0;
	}

	public boolean isGreaterThanOrEqual(MoneyAmount other) {
		if (!this.currency.equals(other.currency)) {
			throw new IllegalArgumentException("cannot compare different currencies");
		}
		return this.amount.compareTo(other.amount) >= 0;
	}

	/**
	 * Retorna representación legible para UI y logs: "15000.00 COP".
	 * Usar {@link #toJsonString()} para serialización hacia APIs externas.
	 */
	public String toDisplayString() {
		return String.format("%s %s", amount.toPlainString(), currency);
	}

	public String toJsonString() {
		return amount.toPlainString();
	}

}
