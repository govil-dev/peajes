package co.quind.peajes.accountmanagement.domain.port.out;

import co.quind.peajes.accountmanagement.domain.model.PrepaidAccount;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import reactor.core.publisher.Mono;

public interface PrepaidAccountRepository {
	Mono<PrepaidAccount> findByAccountId(AccountId accountId);
	Mono<PrepaidAccount> save(PrepaidAccount account);
}
