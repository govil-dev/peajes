package co.quind.peajes.accountmanagement.domain.port.out;

import co.quind.peajes.accountmanagement.domain.event.AccountRecharged;
import reactor.core.publisher.Mono;

public interface AccountEventPublisher {
	Mono<Void> publishAccountRecharged(AccountRecharged event);
}
