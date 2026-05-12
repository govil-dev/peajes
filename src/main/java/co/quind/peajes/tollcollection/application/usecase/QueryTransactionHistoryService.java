package co.quind.peajes.tollcollection.application.usecase;

import co.quind.peajes.tollcollection.application.dto.TransactionHistoryItem;
import co.quind.peajes.tollcollection.application.dto.TransactionHistoryResponse;
import co.quind.peajes.tollcollection.domain.port.in.QueryTransactionHistoryUseCase;
import co.quind.peajes.tollcollection.domain.port.out.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryTransactionHistoryService implements QueryTransactionHistoryUseCase {

    private static final int HISTORY_DAYS = 90;

    private final TransactionHistoryRepository transactionHistoryRepository;

    @Override
    public Mono<TransactionHistoryResponse> queryHistory(String accountId, int page, int size) {
        Instant since = Instant.now().minus(HISTORY_DAYS, ChronoUnit.DAYS);
        int offset = page * size;

        return Mono.zip(
            transactionHistoryRepository.countByAccountIdSince(accountId, since),
            transactionHistoryRepository.findByAccountIdSince(accountId, since, offset, size)
                .map(entry -> new TransactionHistoryItem(
                    entry.transactionId(),
                    entry.stationName(),
                    entry.amount(),
                    entry.vehicleClass(),
                    entry.authorizedAt().toString()
                ))
                .collectList()
        ).map(tuple -> {
            long total = tuple.getT1();
            var items = tuple.getT2();
            int totalPages = total == 0 ? 0 : (int) Math.ceil((double) total / size);
            log.debug("Transaction history: accountId={}, total={}, page={}", accountId, total, page);
            return new TransactionHistoryResponse(items, total, totalPages, page, size);
        }).defaultIfEmpty(TransactionHistoryResponse.empty(page, size));
    }
}
