package co.quind.peajes.accountmanagement.application.usecase;

import co.quind.peajes.accountmanagement.application.command.ProcessPseRechargeCommand;
import co.quind.peajes.accountmanagement.domain.exception.AccountFrozenException;
import co.quind.peajes.accountmanagement.domain.exception.AccountNotFoundException;
import co.quind.peajes.accountmanagement.domain.model.AccountStatus;
import co.quind.peajes.accountmanagement.domain.model.PrepaidAccount;
import co.quind.peajes.accountmanagement.domain.model.RechargeStatus;
import co.quind.peajes.accountmanagement.domain.model.RechargeTransaction;
import co.quind.peajes.accountmanagement.domain.port.in.ProcessPseRechargeUseCase;
import co.quind.peajes.accountmanagement.domain.port.out.AccountEventPublisher;
import co.quind.peajes.accountmanagement.domain.port.out.NotificationPort;
import co.quind.peajes.accountmanagement.domain.port.out.PrepaidAccountRepository;
import co.quind.peajes.accountmanagement.domain.port.out.RechargeTransactionRepository;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import co.quind.peajes.accountmanagement.domain.valueobject.ExternalReferenceId;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPseRechargeServiceTest {

	@Mock private PrepaidAccountRepository accountRepository;
	@Mock private RechargeTransactionRepository transactionRepository;
	@Mock private AccountEventPublisher eventPublisher;
	@Mock private NotificationPort notificationPort;

	private ProcessPseRechargeUseCase useCase;

	private static final AccountId ACCOUNT_ID = AccountId.of("00000000-0000-0000-0000-000000000001");
	private static final Balance INITIAL_BALANCE = Balance.of("50000.00", "COP");
	private static final Balance RECHARGE_AMOUNT = Balance.of("30000.00", "COP");
	private static final ExternalReferenceId EXT_REF = new ExternalReferenceId("PSE-TXN-REF-001");

	@BeforeEach
	void setUp() {
		useCase = new ProcessPseRechargeService(
			accountRepository, transactionRepository,
			eventPublisher, notificationPort,
			new SimpleMeterRegistry());
	}

	@Test
	void recargaExitosaAcreditaSaldoYPublicaEvento() {
		PrepaidAccount account = PrepaidAccount.fromPersisted(
			UUID.randomUUID(), ACCOUNT_ID, INITIAL_BALANCE, AccountStatus.ACTIVE);
		RechargeTransaction tx = RechargeTransaction.completed(ACCOUNT_ID, EXT_REF, RECHARGE_AMOUNT);

		when(transactionRepository.findByExternalReferenceId(EXT_REF)).thenReturn(Mono.empty());
		when(accountRepository.findByAccountId(ACCOUNT_ID)).thenReturn(Mono.just(account));
		when(transactionRepository.save(any())).thenReturn(Mono.just(tx));
		when(accountRepository.save(any())).thenReturn(Mono.just(account));
		when(eventPublisher.publishAccountRecharged(any())).thenReturn(Mono.empty());
		when(notificationPort.sendRechargeConfirmation(any(), any())).thenReturn(Mono.empty());

		StepVerifier.create(useCase.process(buildCommand("COMPLETED", null)))
			.assertNext(result -> {
				assertThat(result.status()).isEqualTo(RechargeStatus.COMPLETED.name());
				assertThat(result.fromCache()).isFalse();
			})
			.verifyComplete();

		verify(eventPublisher).publishAccountRecharged(any());
		verify(notificationPort).sendRechargeConfirmation(any(), any());
		verify(accountRepository).save(any());
	}

	@Test
	void idempotenciaRetornaTransaccionOriginalSinReprocesor() {
		RechargeTransaction existing = RechargeTransaction.completed(ACCOUNT_ID, EXT_REF, RECHARGE_AMOUNT);

		when(transactionRepository.findByExternalReferenceId(EXT_REF)).thenReturn(Mono.just(existing));

		StepVerifier.create(useCase.process(buildCommand("COMPLETED", null)))
			.assertNext(result -> {
				assertThat(result.fromCache()).isTrue();
				assertThat(result.status()).isEqualTo(RechargeStatus.COMPLETED.name());
			})
			.verifyComplete();

		verify(accountRepository, never()).findByAccountId(any());
		verify(eventPublisher, never()).publishAccountRecharged(any());
	}

	@Test
	void cuentaFrozenRechazaRecargaConExcepcion() {
		PrepaidAccount frozenAccount = PrepaidAccount.fromPersisted(
			UUID.randomUUID(), ACCOUNT_ID, INITIAL_BALANCE, AccountStatus.FROZEN);

		when(transactionRepository.findByExternalReferenceId(EXT_REF)).thenReturn(Mono.empty());
		when(accountRepository.findByAccountId(ACCOUNT_ID)).thenReturn(Mono.just(frozenAccount));

		StepVerifier.create(useCase.process(buildCommand("COMPLETED", null)))
			.expectError(AccountFrozenException.class)
			.verify();

		verify(transactionRepository, never()).save(any());
		verify(eventPublisher, never()).publishAccountRecharged(any());
	}

	@Test
	void recargaFallidaPorBancoNoModificaSaldo() {
		PrepaidAccount account = PrepaidAccount.fromPersisted(
			UUID.randomUUID(), ACCOUNT_ID, INITIAL_BALANCE, AccountStatus.ACTIVE);
		RechargeTransaction failedTx = RechargeTransaction.failed(
			ACCOUNT_ID, EXT_REF, RECHARGE_AMOUNT, "INSUFFICIENT_FUNDS_BANK");

		when(transactionRepository.findByExternalReferenceId(EXT_REF)).thenReturn(Mono.empty());
		when(accountRepository.findByAccountId(ACCOUNT_ID)).thenReturn(Mono.just(account));
		when(transactionRepository.save(any())).thenReturn(Mono.just(failedTx));
		when(notificationPort.sendRechargeFailureNotification(any(), any(), any())).thenReturn(Mono.empty());

		StepVerifier.create(useCase.process(buildCommand("FAILED", "INSUFFICIENT_FUNDS_BANK")))
			.assertNext(result -> {
				assertThat(result.status()).isEqualTo(RechargeStatus.FAILED.name());
				assertThat(result.failureReason()).isEqualTo("INSUFFICIENT_FUNDS_BANK");
				assertThat(result.fromCache()).isFalse();
			})
			.verifyComplete();

		verify(accountRepository, never()).save(any());
		verify(eventPublisher, never()).publishAccountRecharged(any());
		verify(notificationPort).sendRechargeFailureNotification(any(), any(), eq("INSUFFICIENT_FUNDS_BANK"));
	}

	@Test
	void cuentaNoEncontradaLanzaExcepcion() {
		when(transactionRepository.findByExternalReferenceId(EXT_REF)).thenReturn(Mono.empty());
		when(accountRepository.findByAccountId(ACCOUNT_ID)).thenReturn(Mono.empty());

		StepVerifier.create(useCase.process(buildCommand("COMPLETED", null)))
			.expectError(AccountNotFoundException.class)
			.verify();
	}

	private ProcessPseRechargeCommand buildCommand(String status, String failureReason) {
		return new ProcessPseRechargeCommand(
			ACCOUNT_ID.toString(),
			EXT_REF.value(),
			RECHARGE_AMOUNT.toPlainString(),
			RECHARGE_AMOUNT.currency(),
			status,
			failureReason
		);
	}
}
