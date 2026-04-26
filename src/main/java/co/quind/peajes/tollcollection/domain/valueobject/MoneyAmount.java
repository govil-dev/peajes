package co.quind.peajes.tollcollection.domain.valueobject;

import co.quind.peajes.tollcollection.domain.exception.InsufficientBalanceException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Representa un monto monetario inmutable con precisión de 2 decimales y moneda asociada.
 * Garantiza no-negatividad en construcción y resta.
 */
public record MoneyAmount(BigDecimal amount, String currency) {

    public MoneyAmount {
        Objects.requireNonNull(amount, "amount es requerido");
        Objects.requireNonNull(currency, "currency es requerida");
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("MoneyAmount no puede ser negativo");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static MoneyAmount of(String amount, String currency) {
        return new MoneyAmount(new BigDecimal(amount), currency);
    }

    public MoneyAmount subtract(MoneyAmount other) {
        Objects.requireNonNull(other, "other es requerido");
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("No se puede restar montos de distinta moneda");
        }
        BigDecimal result = this.amount.subtract(other.amount).setScale(2, RoundingMode.HALF_UP);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException(this, other);
        }
        return new MoneyAmount(result, currency);
    }

    public boolean isLessThan(MoneyAmount other) {
        return this.amount.compareTo(other.amount) < 0;
    }

    /** Representación de cadena decimal usada en APIs y eventos. */
    public String toDecimalString() {
        return amount.toPlainString();
    }
}
