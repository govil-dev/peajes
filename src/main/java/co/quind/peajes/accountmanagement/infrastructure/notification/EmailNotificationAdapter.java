package co.quind.peajes.accountmanagement.infrastructure.notification;

import co.quind.peajes.accountmanagement.domain.port.out.NotificationPort;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class EmailNotificationAdapter implements NotificationPort {

	@Override
	public Mono<Void> sendRechargeConfirmation(AccountId accountId, Balance amount) {
		log.info("Email: recharge confirmation sent to accountId={}, amount={}",
			accountId, amount.toDisplayString());
		return Mono.empty();
	}

	@Override
	public Mono<Void> sendRechargeFailureNotification(AccountId accountId, Balance amount, String reason) {
		log.warn("Email: recharge failure notification sent to accountId={}, amount={}, reason={}",
			accountId, amount.toDisplayString(), reason);
		return Mono.empty();
	}
}
