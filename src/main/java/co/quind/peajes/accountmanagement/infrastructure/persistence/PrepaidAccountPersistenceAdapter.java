package co.quind.peajes.accountmanagement.infrastructure.persistence;

import co.quind.peajes.accountmanagement.domain.model.AccountStatus;
import co.quind.peajes.accountmanagement.domain.model.PrepaidAccount;
import co.quind.peajes.accountmanagement.domain.port.out.PrepaidAccountRepository;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PrepaidAccountPersistenceAdapter implements PrepaidAccountRepository {

	private final PrepaidAccountR2dbcRepository r2dbcRepository;

	public PrepaidAccountPersistenceAdapter(PrepaidAccountR2dbcRepository r2dbcRepository) {
		this.r2dbcRepository = r2dbcRepository;
	}

	@Override
	public Mono<PrepaidAccount> findByAccountId(AccountId accountId) {
		return r2dbcRepository.findByAccountId(accountId.value()).map(this::toDomain);
	}

	@Override
	public Mono<PrepaidAccount> save(PrepaidAccount account) {
		return r2dbcRepository.save(toEntity(account)).map(this::toDomain);
	}

	private PrepaidAccountEntity toEntity(PrepaidAccount a) {
		return new PrepaidAccountEntity(
			a.id(),
			a.accountId().value(),
			a.balance().amount(),
			a.balance().currency(),
			a.status().name()
		);
	}

	private PrepaidAccount toDomain(PrepaidAccountEntity e) {
		return PrepaidAccount.fromPersisted(
			e.id(),
			new AccountId(e.accountId()),
			new Balance(e.balance(), e.currency()),
			AccountStatus.valueOf(e.status())
		);
	}
}
