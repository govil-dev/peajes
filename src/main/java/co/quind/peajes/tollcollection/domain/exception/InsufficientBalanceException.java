package co.quind.peajes.tollcollection.domain.exception;

import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;

/** Se lanza cuando el saldo de la cuenta prepago es insuficiente para cubrir la tarifa. */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(MoneyAmount available, MoneyAmount required) {
        super(String.format("Saldo insuficiente: disponible=%s, requerido=%s %s",
                available.toDecimalString(), required.toDecimalString(), required.currency()));
    }
}
