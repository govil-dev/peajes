package co.quind.peajes.accountmanagement.infrastructure.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PrepaidAccountR2dbcRepository extends ReactiveCrudRepository<PrepaidAccountEntity, UUID> {
	Mono<PrepaidAccountEntity> findByAccountId(UUID accountId);
}
