package co.quind.peajes.accountmanagement.application.usecase;

import co.quind.peajes.accountmanagement.domain.exception.AccountNotFoundException;
import co.quind.peajes.accountmanagement.domain.model.AccountStatus;
import co.quind.peajes.accountmanagement.domain.model.BalanceCacheEntry;
import co.quind.peajes.accountmanagement.domain.model.PrepaidAccount;
import co.quind.peajes.accountmanagement.domain.port.in.QueryBalanceUseCase;
import co.quind.peajes.accountmanagement.domain.port.out.BalanceCachePort;
import co.quind.peajes.accountmanagement.domain.port.out.PrepaidAccountRepository;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueryBalanceServiceTest {

    @Mock private BalanceCachePort balanceCachePort;
    @Mock private PrepaidAccountRepository accountRepository;

    private QueryBalanceUseCase useCase;

    private static final String ACCOUNT_ID = "00000000-0000-0000-0000-000000000001";
    private static final BigDecimal BALANCE = new BigDecimal("50000.00");
    private static final Instant NOW = Instant.parse("2026-05-11T10:00:00Z");

    @BeforeEach
    void setUp() {
        useCase = new QueryBalanceService(balanceCachePort, accountRepository);
    }

    @Test
    void retornaSaldoDesdeCacheWhenCacheHit() {
        BalanceCacheEntry cached = new BalanceCacheEntry(BALANCE, "COP", NOW);
        when(balanceCachePort.get(ACCOUNT_ID)).thenReturn(Mono.just(cached));

        StepVerifier.create(useCase.queryBalance(AccountId.of(ACCOUNT_ID)))
            .assertNext(entry -> {
                assertThat(entry.balance()).isEqualByComparingTo(BALANCE);
                assertThat(entry.currency()).isEqualTo("COP");
                assertThat(entry.lastUpdatedAt()).isEqualTo(NOW);
            })
            .verifyComplete();

        verify(accountRepository, never()).findByAccountId(any());
    }

    @Test
    void retornaSaldoDesdeBdYCacheaWhenCacheMiss() {
        PrepaidAccount account = PrepaidAccount.fromPersisted(
            UUID.randomUUID(),
            AccountId.of(ACCOUNT_ID),
            Balance.of("50000.00", "COP"),
            AccountStatus.ACTIVE
        );
        when(balanceCachePort.get(ACCOUNT_ID)).thenReturn(Mono.empty());
        when(accountRepository.findByAccountId(any())).thenReturn(Mono.just(account));
        when(balanceCachePort.put(eq(ACCOUNT_ID), any())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.queryBalance(AccountId.of(ACCOUNT_ID)))
            .assertNext(entry -> {
                assertThat(entry.balance()).isEqualByComparingTo(BALANCE);
                assertThat(entry.currency()).isEqualTo("COP");
            })
            .verifyComplete();

        verify(balanceCachePort).put(eq(ACCOUNT_ID), any());
    }

    @Test
    void retornaErrorCuandoCuentaNoExiste() {
        when(balanceCachePort.get(ACCOUNT_ID)).thenReturn(Mono.empty());
        when(accountRepository.findByAccountId(any())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.queryBalance(AccountId.of(ACCOUNT_ID)))
            .expectError(AccountNotFoundException.class)
            .verify();
    }
}
