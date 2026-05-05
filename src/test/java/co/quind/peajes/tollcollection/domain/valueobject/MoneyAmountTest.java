package co.quind.peajes.tollcollection.domain.valueobject;

import co.quind.peajes.tollcollection.domain.exception.InsufficientBalanceException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyAmountTest {

	@Test
	void of_validAmount_createsSuccessfully() {
		MoneyAmount amount = MoneyAmount.of(new BigDecimal("100.50"));
		assertEquals(new BigDecimal("100.50"), amount.amount());
		assertEquals("COP", amount.currency());
	}

	@Test
	void of_negativeAmount_throwsIllegalArgument() {
		assertThrows(IllegalArgumentException.class,
			() -> MoneyAmount.of(new BigDecimal("-10")));
	}

	@Test
	void subtract_sufficientBalance_returnsNewAmount() {
		MoneyAmount balance = MoneyAmount.of(new BigDecimal("100"));
		MoneyAmount deduction = MoneyAmount.of(new BigDecimal("30"));

		MoneyAmount result = balance.subtract(deduction);

		assertEquals(new BigDecimal("70.00"), result.amount());
	}

	@Test
	void subtract_insufficientBalance_throwsException() {
		MoneyAmount balance = MoneyAmount.of(new BigDecimal("50"));
		MoneyAmount deduction = MoneyAmount.of(new BigDecimal("100"));

		assertThrows(InsufficientBalanceException.class, () -> balance.subtract(deduction));
	}

	@Test
	void isLessThan_returnsTrue() {
		MoneyAmount a = MoneyAmount.of(new BigDecimal("10"));
		MoneyAmount b = MoneyAmount.of(new BigDecimal("20"));

		assertTrue(a.isLessThan(b));
		assertFalse(b.isLessThan(a));
	}

}
