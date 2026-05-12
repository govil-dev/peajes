package co.quind.peajes.tollcollection.domain.port.in;

import co.quind.peajes.tollcollection.application.dto.TransactionHistoryResponse;
import reactor.core.publisher.Mono;

public interface QueryTransactionHistoryUseCase {
    Mono<TransactionHistoryResponse> queryHistory(String accountId, int page, int size);
}
