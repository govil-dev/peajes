package co.quind.peajes.tollcollection.application.usecase;

import co.quind.peajes.tollcollection.domain.model.TcTransactionEntry;
import co.quind.peajes.tollcollection.domain.port.in.QueryTransactionHistoryUseCase;
import co.quind.peajes.tollcollection.domain.port.out.TransactionHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryTransactionHistoryServiceTest {

    @Mock private TransactionHistoryRepository transactionHistoryRepository;

    private QueryTransactionHistoryUseCase useCase;

    private static final String ACCOUNT_ID = "00000000-0000-0000-0000-000000000001";

    @BeforeEach
    void setUp() {
        useCase = new QueryTransactionHistoryService(transactionHistoryRepository);
    }

    @Test
    void retornaHistorialPaginado() {
        TcTransactionEntry entry = new TcTransactionEntry(
            "pass-001", ACCOUNT_ID, "ST-BOG-001",
            new BigDecimal("9500.00"), "COP", "CLASS_I",
            Instant.parse("2026-05-10T10:00:00Z")
        );

        when(transactionHistoryRepository.countByAccountIdSince(eq(ACCOUNT_ID), any()))
            .thenReturn(Mono.just(1L));
        when(transactionHistoryRepository.findByAccountIdSince(eq(ACCOUNT_ID), any(), anyInt(), anyInt()))
            .thenReturn(Flux.just(entry));

        StepVerifier.create(useCase.queryHistory(ACCOUNT_ID, 0, 20))
            .assertNext(response -> {
                assertThat(response.totalElements()).isEqualTo(1L);
                assertThat(response.totalPages()).isEqualTo(1);
                assertThat(response.pageNumber()).isEqualTo(0);
                assertThat(response.pageSize()).isEqualTo(20);
                assertThat(response.content()).hasSize(1);
                assertThat(response.content().get(0).transactionId()).isEqualTo("pass-001");
                assertThat(response.content().get(0).stationName()).isEqualTo("ST-BOG-001");
                assertThat(response.content().get(0).vehicleClass()).isEqualTo("CLASS_I");
            })
            .verifyComplete();
    }

    @Test
    void retornaListaVaciaCuandoNoHayCobros() {
        when(transactionHistoryRepository.countByAccountIdSince(eq(ACCOUNT_ID), any()))
            .thenReturn(Mono.just(0L));
        when(transactionHistoryRepository.findByAccountIdSince(eq(ACCOUNT_ID), any(), anyInt(), anyInt()))
            .thenReturn(Flux.empty());

        StepVerifier.create(useCase.queryHistory(ACCOUNT_ID, 0, 20))
            .assertNext(response -> {
                assertThat(response.totalElements()).isEqualTo(0L);
                assertThat(response.totalPages()).isEqualTo(0);
                assertThat(response.content()).isEmpty();
            })
            .verifyComplete();
    }

    @Test
    void calculaPaginacionCorrectamente() {
        when(transactionHistoryRepository.countByAccountIdSince(eq(ACCOUNT_ID), any()))
            .thenReturn(Mono.just(45L));
        when(transactionHistoryRepository.findByAccountIdSince(eq(ACCOUNT_ID), any(), anyInt(), anyInt()))
            .thenReturn(Flux.empty());

        StepVerifier.create(useCase.queryHistory(ACCOUNT_ID, 2, 20))
            .assertNext(response -> {
                assertThat(response.totalElements()).isEqualTo(45L);
                assertThat(response.totalPages()).isEqualTo(3);
                assertThat(response.pageNumber()).isEqualTo(2);
                assertThat(response.pageSize()).isEqualTo(20);
            })
            .verifyComplete();
    }
}
