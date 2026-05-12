package co.quind.peajes.tollcollection.infrastructure.persistence;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

public interface TcTransactionHistoryR2dbcRepository
        extends ReactiveCrudRepository<TcTransactionHistoryEntity, UUID> {

    @Query("SELECT * FROM tc_transaction_history_rm WHERE account_id = :accountId AND authorized_at >= :since ORDER BY authorized_at DESC LIMIT :limit OFFSET :offset")
    Flux<TcTransactionHistoryEntity> findByAccountIdSince(
        String accountId, Instant since, int offset, int limit);

    @Query("SELECT COUNT(*) FROM tc_transaction_history_rm WHERE account_id = :accountId AND authorized_at >= :since")
    Mono<Long> countByAccountIdSince(String accountId, Instant since);

    Mono<Boolean> existsByTransactionId(String transactionId);
}
