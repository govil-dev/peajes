package co.quind.peajes.accountmanagement.application.usecase;

import co.quind.peajes.accountmanagement.application.command.InitiatePseRechargeCommand;
import co.quind.peajes.accountmanagement.application.dto.RechargeInitiatedResponse;
import co.quind.peajes.accountmanagement.domain.exception.AccountFrozenException;
import co.quind.peajes.accountmanagement.domain.exception.AccountNotFoundException;
import co.quind.peajes.accountmanagement.domain.port.in.InitiatePseRechargeUseCase;
import co.quind.peajes.accountmanagement.domain.port.out.PaymentGatewayPort;
import co.quind.peajes.accountmanagement.domain.port.out.PrepaidAccountRepository;
import co.quind.peajes.accountmanagement.domain.model.AccountStatus;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitiatePseRechargeService implements InitiatePseRechargeUseCase {

	private final PrepaidAccountRepository accountRepository;
	private final PaymentGatewayPort paymentGatewayPort;

	@Override
	public Mono<RechargeInitiatedResponse> initiate(InitiatePseRechargeCommand command) {
		AccountId accountId = AccountId.of(command.accountId());
		Balance amount = Balance.of(command.amount(), command.currency());

		log.info("Initiating PSE recharge: accountId={}, amount={}", accountId, amount.toDisplayString());

		return accountRepository.findByAccountId(accountId)
			.switchIfEmpty(Mono.error(new AccountNotFoundException(accountId.toString())))
			.flatMap(account -> {
				if (account.status() == AccountStatus.FROZEN) {
					return Mono.error(new AccountFrozenException(accountId.toString()));
				}
				return paymentGatewayPort.initiatePayment(accountId, amount);
			})
			.map(session -> {
				log.info("PSE payment session created: accountId={}, externalRef={}",
					accountId, new co.quind.peajes.accountmanagement.domain.valueobject.ExternalReferenceId(
						session.externalReferenceId()).masked());
				return new RechargeInitiatedResponse(
					accountId.toString(),
					session.externalReferenceId(),
					session.redirectUrl()
				);
			});
	}
}
