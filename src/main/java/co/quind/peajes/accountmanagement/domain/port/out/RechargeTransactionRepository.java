package co.quind.peajes.accountmanagement.domain.port.out;

import co.quind.peajes.accountmanagement.domain.model.RechargeTransaction;
import co.quind.peajes.accountmanagement.domain.valueobject.ExternalReferenceId;
import reactor.core.publisher.Mono;

public interface RechargeTransactionRepository {
	Mono<RechargeTransaction> findByExternalReferenceId(ExternalReferenceId externalReferenceId);
	Mono<RechargeTransaction> save(RechargeTransaction transaction);
}
