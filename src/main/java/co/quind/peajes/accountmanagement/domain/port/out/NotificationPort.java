package co.quind.peajes.accountmanagement.domain.port.out;

import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import reactor.core.publisher.Mono;

public interface NotificationPort {
	Mono<Void> sendRechargeConfirmation(AccountId accountId, Balance amount);
	Mono<Void> sendRechargeFailureNotification(AccountId accountId, Balance amount, String reason);
}
