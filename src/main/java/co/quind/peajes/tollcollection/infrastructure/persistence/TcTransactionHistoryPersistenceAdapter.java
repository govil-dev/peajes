package co.quind.peajes.tollcollection.infrastructure.persistence;

import co.quind.peajes.tollcollection.domain.model.TcTransactionEntry;
import co.quind.peajes.tollcollection.domain.port.out.TransactionHistoryRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Component
public class TcTransactionHistoryPersistenceAdapter implements TransactionHistoryRepository {

    private final TcTransactionHistoryR2dbcRepository r2dbcRepository;

    public TcTransactionHistoryPersistenceAdapter(TcTransactionHistoryR2dbcRepository r2dbcRepository) {
        this.r2dbcRepository = r2dbcRepository;
    }

    @Override
    public Flux<TcTransactionEntry> findByAccountIdSince(String accountId, Instant since, int offset, int limit) {
        return r2dbcRepository.findByAccountIdSince(accountId, since, offset, limit)
            .map(this::toDomain);
    }

    @Override
    public Mono<Long> countByAccountIdSince(String accountId, Instant since) {
        return r2dbcRepository.countByAccountIdSince(accountId, since);
    }

    @Override
    public Mono<Boolean> existsByTransactionId(String transactionId) {
        return r2dbcRepository.existsByTransactionId(transactionId);
    }

    @Override
    public Mono<Void> save(TcTransactionEntry entry) {
        TcTransactionHistoryEntity entity = new TcTransactionHistoryEntity(
            UUID.randomUUID(),
            entry.transactionId(),
            entry.accountId(),
            entry.stationName(),
            entry.amount(),
            entry.currency(),
            entry.vehicleClass(),
            entry.authorizedAt()
        );
        return r2dbcRepository.save(entity).then();
    }

    private TcTransactionEntry toDomain(TcTransactionHistoryEntity e) {
        return new TcTransactionEntry(
            e.transactionId(),
            e.accountId(),
            e.stationName(),
            e.amount(),
            e.currency(),
            e.vehicleClass(),
            e.authorizedAt()
        );
    }
}
