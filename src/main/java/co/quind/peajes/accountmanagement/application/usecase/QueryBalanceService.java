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
    public Mono<BalanceCacheEntry> queryBalance(String accountId) {
        return balanceCachePort.get(accountId)
            .doOnNext(e -> log.debug("Balance cache hit: accountId={}", accountId))
            .switchIfEmpty(Mono.defer(() -> loadFromDbAndCache(accountId)));
    }

    private Mono<BalanceCacheEntry> loadFromDbAndCache(String accountId) {
        log.debug("Balance cache miss — loading from DB: accountId={}", accountId);
        return accountRepository.findByAccountId(AccountId.of(accountId))
            .switchIfEmpty(Mono.error(new AccountNotFoundException(accountId)))
            .flatMap(account -> {
                BalanceCacheEntry entry = new BalanceCacheEntry(
                    account.balance().amount(),
                    account.balance().currency(),
                    Instant.now()
                );
                return balanceCachePort.put(accountId, entry).thenReturn(entry);
            });
    }
}
