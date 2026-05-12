package co.quind.peajes.accountmanagement.domain.model;

import co.quind.peajes.accountmanagement.domain.event.AccountRecharged;
import co.quind.peajes.accountmanagement.domain.exception.AccountFrozenException;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import co.quind.peajes.accountmanagement.domain.valueobject.ExternalReferenceId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class PrepaidAccountTest {

	private static final AccountId ACCOUNT_ID = AccountId.of("00000000-0000-0000-0000-000000000001");
	private static final Balance INITIAL_BALANCE = Balance.of("50000.00", "COP");
	private static final Balance RECHARGE_AMOUNT = Balance.of("30000.00", "COP");
	private static final ExternalReferenceId EXT_REF = new ExternalReferenceId("PSE-TXN-001");

	@Test
	void recargarCuentaActivaAcreditaSaldoYGeneraEvento() {
		PrepaidAccount account = PrepaidAccount.fromPersisted(UUID.randomUUID(), ACCOUNT_ID,
			INITIAL_BALANCE, AccountStatus.ACTIVE);

		RechargeTransaction tx = account.creditRecharge(EXT_REF, RECHARGE_AMOUNT);

		assertThat(account.balance().amount().toPlainString()).isEqualTo("80000.00");
		assertThat(tx.status()).isEqualTo(RechargeStatus.COMPLETED);
		assertThat(tx.externalReferenceId()).isEqualTo(EXT_REF);

		var events = account.pullDomainEvents();
		assertThat(events).hasSize(1);
		assertThat(events.get(0)).isInstanceOf(AccountRecharged.class);
		AccountRecharged event = (AccountRecharged) events.get(0);
		assertThat(event.source()).isEqualTo("PSE");
		assertThat(event.balanceAfter()).isEqualTo("80000.00");
		assertThat(event.accountId()).isEqualTo(ACCOUNT_ID.toString());
	}

	@Test
	void recargarCuentaFrozenLanzaExcepcion() {
		PrepaidAccount account = PrepaidAccount.fromPersisted(UUID.randomUUID(), ACCOUNT_ID,
			INITIAL_BALANCE, AccountStatus.FROZEN);

		assertThatThrownBy(() -> account.creditRecharge(EXT_REF, RECHARGE_AMOUNT))
			.isInstanceOf(AccountFrozenException.class)
			.hasMessageContaining("FROZEN");

		assertThat(account.balance()).isEqualTo(INITIAL_BALANCE);
		assertThat(account.pullDomainEvents()).isEmpty();
	}

	@Test
	void registrarRecargaFallidaNoModificaSaldo() {
		PrepaidAccount account = PrepaidAccount.fromPersisted(UUID.randomUUID(), ACCOUNT_ID,
			INITIAL_BALANCE, AccountStatus.ACTIVE);

		RechargeTransaction tx = account.registerFailedRecharge(EXT_REF, RECHARGE_AMOUNT, "INSUFFICIENT_FUNDS_BANK");

		assertThat(account.balance()).isEqualTo(INITIAL_BALANCE);
		assertThat(tx.status()).isEqualTo(RechargeStatus.FAILED);
		assertThat(tx.failureReason()).isEqualTo("INSUFFICIENT_FUNDS_BANK");
		assertThat(account.pullDomainEvents()).isEmpty();
	}

	@Test
	void pullDomainEventsLimpiaLaListaInterna() {
		PrepaidAccount account = PrepaidAccount.fromPersisted(UUID.randomUUID(), ACCOUNT_ID,
			INITIAL_BALANCE, AccountStatus.ACTIVE);
		account.creditRecharge(EXT_REF, RECHARGE_AMOUNT);

		var firstPull = account.pullDomainEvents();
		var secondPull = account.pullDomainEvents();

		assertThat(firstPull).hasSize(1);
		assertThat(secondPull).isEmpty();
	}
}
