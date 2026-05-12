package co.quind.peajes.accountmanagement.domain.port.in;

import co.quind.peajes.accountmanagement.domain.model.BalanceCacheEntry;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import reactor.core.publisher.Mono;

public interface QueryBalanceUseCase {
    Mono<BalanceCacheEntry> queryBalance(AccountId accountId);
}
