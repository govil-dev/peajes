package co.quind.peajes.accountmanagement.domain.port.out;

import co.quind.peajes.accountmanagement.domain.model.BalanceCacheEntry;
import reactor.core.publisher.Mono;

public interface BalanceCachePort {
    Mono<BalanceCacheEntry> get(String accountId);
    Mono<Void> put(String accountId, BalanceCacheEntry entry);
}
