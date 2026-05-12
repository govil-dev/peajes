package co.quind.peajes.accountmanagement.infrastructure.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RechargeTransactionR2dbcRepository extends ReactiveCrudRepository<RechargeTransactionEntity, UUID> {
	Mono<RechargeTransactionEntity> findByExternalReferenceId(String externalReferenceId);
}
