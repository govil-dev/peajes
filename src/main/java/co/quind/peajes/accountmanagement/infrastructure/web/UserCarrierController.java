package co.quind.peajes.accountmanagement.infrastructure.web;

import co.quind.peajes.accountmanagement.application.dto.BalanceResponse;
import co.quind.peajes.accountmanagement.domain.port.in.QueryBalanceUseCase;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserCarrierController {

    private final QueryBalanceUseCase queryBalanceUseCase;

    @GetMapping("/api/v1/user-carriers/{userCarrierId}/balance")
    public Mono<BalanceResponse> getBalance(@PathVariable String userCarrierId) {
        log.info("Balance query: userCarrierId={}", userCarrierId);
        return queryBalanceUseCase.queryBalance(AccountId.of(userCarrierId))
            .map(e -> new BalanceResponse(
                e.balance().toPlainString(),
                e.currency(),
                e.lastUpdatedAt().toString()
            ));
    }
}
