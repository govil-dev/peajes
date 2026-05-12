package co.quind.peajes.accountmanagement.application.usecase;

import co.quind.peajes.accountmanagement.application.command.ProcessPseRechargeCommand;
import co.quind.peajes.accountmanagement.application.dto.PseRechargeResponse;
import co.quind.peajes.accountmanagement.domain.exception.AccountNotFoundException;
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
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessPseRechargeService implements ProcessPseRechargeUseCase {

	private static final String METRIC_RECHARGE_AMOUNT = "account.recharge.amount";
	private static final String SOURCE_PSE = "PSE";

	private final PrepaidAccountRepository accountRepository;
	private final RechargeTransactionRepository transactionRepository;
	private final AccountEventPublisher eventPublisher;
	private final NotificationPort notificationPort;
	private final MeterRegistry meterRegistry;

	@Override
	public Mono<PseRechargeResponse> process(ProcessPseRechargeCommand command) {
		AccountId accountId = AccountId.of(command.accountId());
		ExternalReferenceId externalRef = new ExternalReferenceId(command.externalReferenceId());
		Balance amount = Balance.of(command.amount(), command.currency());

		log.info("Processing PSE recharge notification: accountId={}, externalRef={}, status={}",
			accountId, externalRef.masked(), command.paymentStatus());

		return transactionRepository.findByExternalReferenceId(externalRef)
			.map(existing -> {
				log.info("Recharge already processed (idempotent): externalRef={}", externalRef.masked());
				return PseRechargeResponse.from(existing, true);
			})
			.switchIfEmpty(Mono.defer(() -> processNew(accountId, externalRef, amount, command)));
	}

	private Mono<PseRechargeResponse> processNew(AccountId accountId, ExternalReferenceId externalRef,
												  Balance amount, ProcessPseRechargeCommand command) {
		return accountRepository.findByAccountId(accountId)
			.switchIfEmpty(Mono.error(new AccountNotFoundException(accountId.toString())))
			.flatMap(account -> {
				if (RechargeStatus.FAILED.name().equals(command.paymentStatus())) {
					return handleFailedRecharge(account, externalRef, amount, command.failureReason());
				}
				return handleSuccessfulRecharge(account, externalRef, amount);
			});
	}

	private Mono<PseRechargeResponse> handleSuccessfulRecharge(PrepaidAccount account,
															   ExternalReferenceId externalRef,
															   Balance amount) {
		return Mono.fromCallable(() -> account.creditRecharge(externalRef, amount))
			.flatMap(transaction -> {
				List<Object> events = account.pullDomainEvents();
				return transactionRepository.save(transaction)
					.flatMap(saved -> accountRepository.save(account).thenReturn(saved))
					.flatMap(saved -> publishEvents(events).thenReturn(saved))
					.flatMap(saved -> notificationPort
						.sendRechargeConfirmation(account.accountId(), amount)
						.thenReturn(saved))
					.map(saved -> {
						recordMetric(amount, SOURCE_PSE, "SUCCESS");
						log.info("PSE recharge completed: accountId={}, amount={}, balanceAfter={}",
							account.accountId(), amount.toDisplayString(),
							account.balance().toDisplayString());
						return PseRechargeResponse.from(saved, false);
					});
			});
	}

	private Mono<PseRechargeResponse> handleFailedRecharge(PrepaidAccount account,
														   ExternalReferenceId externalRef,
														   Balance amount, String failureReason) {
		RechargeTransaction transaction = account.registerFailedRecharge(externalRef, amount, failureReason);
		return transactionRepository.save(transaction)
			.flatMap(saved -> notificationPort
				.sendRechargeFailureNotification(account.accountId(), amount, failureReason)
				.thenReturn(saved))
			.map(saved -> {
				recordMetric(amount, SOURCE_PSE, "FAILED");
				log.warn("PSE recharge failed: accountId={}, amount={}, reason={}",
					account.accountId(), amount.toDisplayString(), failureReason);
				return PseRechargeResponse.from(saved, false);
			});
	}

	private Mono<Void> publishEvents(List<Object> events) {
		return Flux.fromIterable(events)
			.ofType(co.quind.peajes.accountmanagement.domain.event.AccountRecharged.class)
			.flatMap(eventPublisher::publishAccountRecharged)
			.then();
	}

	private void recordMetric(Balance amount, String source, String status) {
		meterRegistry.summary(METRIC_RECHARGE_AMOUNT,
				"source", source,
				"status", status)
			.record(amount.amount().doubleValue());
	}
}
