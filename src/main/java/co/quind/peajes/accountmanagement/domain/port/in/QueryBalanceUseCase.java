package co.quind.peajes.accountmanagement.domain.port.in;

import co.quind.peajes.accountmanagement.domain.model.BalanceCacheEntry;
import reactor.core.publisher.Mono;

public interface QueryBalanceUseCase {
    Mono<BalanceCacheEntry> queryBalance(String accountId);
}
