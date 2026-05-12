package co.quind.peajes.accountmanagement.infrastructure.adapter.payu;

import co.quind.peajes.accountmanagement.application.dto.PsePaymentSession;
import co.quind.peajes.accountmanagement.domain.exception.PaymentGatewayException;
import co.quind.peajes.accountmanagement.domain.port.out.PaymentGatewayPort;
import co.quind.peajes.accountmanagement.domain.valueobject.AccountId;
import co.quind.peajes.accountmanagement.domain.valueobject.Balance;
import co.quind.peajes.accountmanagement.infrastructure.adapter.payu.dto.PayUTransactionRequest;
import co.quind.peajes.accountmanagement.infrastructure.adapter.payu.dto.PayUTransactionResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * Adaptador hacia PayU SDK para PSE.
 * Aplica circuit breaker (failureRateThreshold=60%, wait=60s), timeout 3s y retry (x1, backoff 500ms).
 */
@Slf4j
@Component
public class PayUGatewayAdapter implements PaymentGatewayPort {

	private static final String CB_NAME = "payu-gateway";

	private final WebClient webClient;

	public PayUGatewayAdapter(WebClient payuWebClient) {
		this.webClient = payuWebClient;
	}

	@Override
	@CircuitBreaker(name = CB_NAME, fallbackMethod = "fallbackInitiatePayment")
	public Mono<PsePaymentSession> initiatePayment(AccountId accountId, Balance amount) {
		var request = new PayUTransactionRequest(
			accountId.toString(),
			amount.toPlainString(),
			amount.currency(),
			"Recarga PSE cuenta prepago peajes"
		);

		return webClient.post()
			.uri("/api/v1/pse/transactions")
			.bodyValue(request)
			.retrieve()
			.bodyToMono(PayUTransactionResponse.class)
			.timeout(Duration.ofSeconds(3))
			.retryWhen(Retry.backoff(1, Duration.ofMillis(500))
				.filter(ex -> !(ex instanceof PaymentGatewayException)))
			.map(response -> new PsePaymentSession(response.externalReferenceId(), response.redirectUrl()))
			.doOnError(ex -> log.error("PayU gateway error for accountId={}: {}", accountId, ex.getMessage()));
	}

	@SuppressWarnings("unused")
	private Mono<PsePaymentSession> fallbackInitiatePayment(AccountId accountId, Balance amount, Throwable ex) {
		log.error("PayU circuit breaker open for accountId={}: {}", accountId, ex.getMessage());
		return Mono.error(new PaymentGatewayException(
			"El servicio de pagos PSE no está disponible en este momento. Intente nuevamente más tarde.", ex));
	}
}
