package co.quind.peajes.accountmanagement.application.usecase;

import co.quind.peajes.accountmanagement.domain.exception.AccountNotFoundException;
import co.quind.peajes.accountmanagement.domain.model.BalanceCacheEntry;
import co.quind.peajes.accountmanagement.domain.port.in.QueryBalanceUseCase;
import co.quind.peajes.accountmanagement.domain.port.out.BalanceCachePort;
import co.quind.peajes.accountmanagement.domain.port.out.PrepaidAccountRepository;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryBalanceService implements QueryBalanceUseCase {

    private final BalanceCachePort balanceCachePort;
    private final PrepaidAccountRepository accountRepository;

    @Override
    public Mono<BalanceCacheEntry> queryBalance(AccountId accountId) {
        String id = accountId.toString();
        return balanceCachePort.get(id)
            .doOnNext(e -> log.debug("Balance cache hit: accountId={}", id))
            .switchIfEmpty(Mono.defer(() -> loadFromDbAndCache(accountId)));
    }

    private Mono<BalanceCacheEntry> loadFromDbAndCache(AccountId accountId) {
        log.debug("Balance cache miss — loading from DB: accountId={}", accountId);
        return accountRepository.findByAccountId(accountId)
            .switchIfEmpty(Mono.error(new AccountNotFoundException(accountId.toString())))
            .flatMap(account -> {
                BalanceCacheEntry entry = new BalanceCacheEntry(
                    account.balance().amount(),
                    account.balance().currency(),
                    Instant.now()
                );
                return balanceCachePort.put(accountId.toString(), entry).thenReturn(entry);
            });
    }
}
