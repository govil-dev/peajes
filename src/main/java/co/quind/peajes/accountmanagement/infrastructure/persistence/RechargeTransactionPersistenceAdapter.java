package co.quind.peajes.accountmanagement.infrastructure.persistence;

import co.quind.peajes.accountmanagement.domain.model.RechargeStatus;
import co.quind.peajes.accountmanagement.domain.model.RechargeTransaction;
import co.quind.peajes.accountmanagement.domain.port.out.RechargeTransactionRepository;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import co.quind.peajes.accountmanagement.domain.valueobject.ExternalReferenceId;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RechargeTransactionPersistenceAdapter implements RechargeTransactionRepository {

	private final RechargeTransactionR2dbcRepository r2dbcRepository;

	public RechargeTransactionPersistenceAdapter(RechargeTransactionR2dbcRepository r2dbcRepository) {
		this.r2dbcRepository = r2dbcRepository;
	}

	@Override
	public Mono<RechargeTransaction> findByExternalReferenceId(ExternalReferenceId externalReferenceId) {
		return r2dbcRepository.findByExternalReferenceId(externalReferenceId.value()).map(this::toDomain);
	}

	@Override
	public Mono<RechargeTransaction> save(RechargeTransaction transaction) {
		return r2dbcRepository.save(toEntity(transaction)).map(this::toDomain);
	}

	private RechargeTransactionEntity toEntity(RechargeTransaction t) {
		return new RechargeTransactionEntity(
			t.id(),
			t.accountId().value(),
			t.externalReferenceId().value(),
			t.amount().amount(),
			t.amount().currency(),
			t.status().name(),
			t.failureReason(),
			t.createdAt()
		);
	}

	private RechargeTransaction toDomain(RechargeTransactionEntity e) {
		return RechargeTransaction.fromPersisted(
			e.id(),
			new AccountId(e.accountId()),
			new ExternalReferenceId(e.externalReferenceId()),
			new Balance(e.amount(), e.currency()),
			RechargeStatus.valueOf(e.status()),
			e.failureReason(),
			e.createdAt()
		);
	}
}
