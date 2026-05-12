package co.quind.peajes.tollcollection.infrastructure.web;

import co.quind.peajes.tollcollection.application.dto.TransactionHistoryResponse;
import co.quind.peajes.tollcollection.domain.port.in.QueryTransactionHistoryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserCarrierTransactionController {

    private final QueryTransactionHistoryUseCase queryTransactionHistoryUseCase;

    @GetMapping("/api/v1/user-carriers/{userCarrierId}/transactions")
    public Mono<TransactionHistoryResponse> getTransactionHistory(
            @PathVariable String userCarrierId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Transaction history query: userCarrierId={}, page={}, size={}", userCarrierId, page, size);
        return queryTransactionHistoryUseCase.queryHistory(userCarrierId, page, size);
    }
}
