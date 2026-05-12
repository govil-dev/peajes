package co.quind.peajes.tollcollection.infrastructure.adapter.account;

import co.quind.peajes.tollcollection.application.dto.AccountDetails;
import co.quind.peajes.tollcollection.domain.model.TagStatus;
import co.quind.peajes.tollcollection.domain.port.out.AccountManagementPort;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.PassId;
import co.quind.peajes.tollcollection.domain.valueobject.TagId;
import co.quind.peajes.tollcollection.infrastructure.adapter.account.dto.AccountApiResponse;
import co.quind.peajes.tollcollection.infrastructure.adapter.account.dto.DeductionApiResponse;
import co.quind.peajes.tollcollection.infrastructure.adapter.account.dto.DeductionRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Adaptador hacia account-management-api.
 * Aplica circuit breaker (failureRateThreshold=50%) y timeout de 200ms.
 */
@Component
public class AccountManagementAdapter implements AccountManagementPort {

    private static final Logger log = LoggerFactory.getLogger(AccountManagementAdapter.class);
    private static final String CB_NAME = "account-management";

    private final WebClient webClient;

    public AccountManagementAdapter(WebClient accountManagementWebClient) {
        this.webClient = accountManagementWebClient;
    }

    @Override
    @CircuitBreaker(name = CB_NAME)
    @TimeLimiter(name = CB_NAME)
    public Mono<AccountDetails> getAccountByTag(TagId tagId) {
        return webClient.get()
                .uri("/api/v1/accounts/tags/{tagId}", tagId.value())
                .retrieve()
                .bodyToMono(AccountApiResponse.class)
                .map(r -> new AccountDetails(
                        r.accountId(),
                        new TagId(r.tagId()),
                        TagStatus.valueOf(r.tagStatus()),
                        MoneyAmount.of(r.balance(), r.currency())))
                .doOnError(e -> log.error("Error obteniendo cuenta para tag {}: {}",
                        tagId.toMasked(), e.getMessage()));
    }

    @Override
    @CircuitBreaker(name = CB_NAME)
    @TimeLimiter(name = CB_NAME)
    public Mono<MoneyAmount> deductBalance(TagId tagId, MoneyAmount amount, PassId passId) {
        var request = new DeductionRequest(passId.value(), amount.toJsonString(), amount.currency());
        return webClient.post()
                .uri("/api/v1/accounts/tags/{tagId}/deductions", tagId.value())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DeductionApiResponse.class)
                .map(r -> MoneyAmount.of(r.newBalance(), r.currency()))
                .doOnError(e -> log.error("Error debitando cuenta para tag {}: {}",
                        tagId.toMasked(), e.getMessage()));
    }
}
