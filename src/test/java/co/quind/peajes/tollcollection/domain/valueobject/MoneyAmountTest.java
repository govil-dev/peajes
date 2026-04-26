package co.quind.peajes.tollcollection.domain.valueobject;

import co.quind.peajes.tollcollection.domain.exception.InsufficientBalanceException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class MoneyAmountTest {

    @Test
    void debeCrearseConEscala2() {
        var amount = MoneyAmount.of("9500", "COP");
        assertThat(amount.amount()).isEqualByComparingTo(new BigDecimal("9500.00"));
        assertThat(amount.currency()).isEqualTo("COP");
    }

    @Test
    void debeRechazarMontoNegativo() {
        assertThatThrownBy(() -> MoneyAmount.of("-1.00", "COP"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void debeRestarCorrectamente() {
        var balance = MoneyAmount.of("50000.00", "COP");
        var tariff = MoneyAmount.of("9500.00", "COP");
        var result = balance.subtract(tariff);
        assertThat(result.toDecimalString()).isEqualTo("40500.00");
    }

    @Test
    void debeLanzarInsufficientBalanceAlRestar() {
        var balance = MoneyAmount.of("5000.00", "COP");
        var tariff = MoneyAmount.of("9500.00", "COP");
        assertThatThrownBy(() -> balance.subtract(tariff))
                .isInstanceOf(InsufficientBalanceException.class);
    }

    @Test
    void debeDetectarSaldoBajo() {
        var balance = MoneyAmount.of("10000.00", "COP");
        var threshold = MoneyAmount.of("15000.00", "COP");
        assertThat(balance.isLessThan(threshold)).isTrue();
    }

    @Test
    void debeMascararTagId() {
        var tagId = new TagId("A1B2C3D4");
        assertThat(tagId.toMasked()).isEqualTo("****C3D4");
    }
}
