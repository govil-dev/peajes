package co.quind.peajes.tollcollection.infrastructure.webclient;

import co.quind.peajes.tollcollection.domain.exception.InsufficientBalanceException;
import co.quind.peajes.tollcollection.domain.model.PrepaidAccountSnapshot;
import co.quind.peajes.tollcollection.domain.port.out.AccountManagementPort;
import co.quind.peajes.tollcollection.domain.valueobject.AccountId;
import co.quind.peajes.tollcollection.domain.valueobject.MoneyAmount;
import co.quind.peajes.tollcollection.domain.valueobject.PassId;
import co.quind.peajes.tollcollection.domain.valueobject.TagId;
import co.quind.peajes.tollcollection.infrastructure.webclient.dto.AccountBalanceResponse;
import co.quind.peajes.tollcollection.infrastructure.webclient.dto.DeductBalanceRequest;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountManagementClient implements AccountManagementPort {

	private final WebClient.Builder webClientBuilder;
	private final CircuitBreaker circuitBreaker;

	@Value("${app.account-management.base-url:http://localhost:8081}")
	private String baseUrl;

	@Override
	public Mono<PrepaidAccountSnapshot> getBalance(TagId tagId) {
		return webClientBuilder.baseUrl(baseUrl).build()
			.get()
			.uri("/internal/v1/accounts/by-tag/{tagId}/balance", tagId.value())
			.retrieve()
			.bodyToMono(AccountBalanceResponse.class)
			.map(resp -> new PrepaidAccountSnapshot(
				AccountId.of(resp.accountId()),
				MoneyAmount.of(resp.balance())
			))
			.timeout(Duration.ofMillis(200))
			.transform(CircuitBreakerOperator.of(circuitBreaker));
	}

	@Override
	public Mono<PrepaidAccountSnapshot> deductBalance(TagId tagId, MoneyAmount amount, PassId passId) {
		DeductBalanceRequest body = new DeductBalanceRequest(
			amount.amount().toPlainString(),
			amount.currency(),
			passId.value()
		);

		return webClientBuilder.baseUrl(baseUrl).build()
			.post()
			.uri("/internal/v1/accounts/by-tag/{tagId}/deductions", tagId.value())
			.bodyValue(body)
			.retrieve()
			.onStatus(status -> status.equals(HttpStatus.CONFLICT),
				resp -> resp.bodyToMono(String.class)
					.flatMap(b -> Mono.error(new InsufficientBalanceException("insufficient balance")))
			)
			.bodyToMono(AccountBalanceResponse.class)
			.map(resp -> new PrepaidAccountSnapshot(
				AccountId.of(resp.accountId()),
				MoneyAmount.of(resp.balance())
			))
			.timeout(Duration.ofMillis(200))
			.doOnError(ex -> log.warn("Account management error: {}", ex.getMessage()))
			.transform(CircuitBreakerOperator.of(circuitBreaker));
	}

}
