package co.quind.peajes.tollcollection.domain.port.out;

import co.quind.peajes.tollcollection.domain.model.TcTransactionEntry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

public interface TransactionHistoryRepository {
    Flux<TcTransactionEntry> findByAccountIdSince(String accountId, Instant since, int offset, int limit);
    Mono<Long> countByAccountIdSince(String accountId, Instant since);
    Mono<Void> save(TcTransactionEntry entry);
    Mono<Boolean> existsByTransactionId(String transactionId);
}
