package co.quind.peajes.accountmanagement.infrastructure.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AccountManagementResilienceConfig {

	@Bean("payuCircuitBreakerRegistry")
	public CircuitBreakerRegistry payuCircuitBreakerRegistry() {
		CircuitBreakerConfig config = CircuitBreakerConfig.custom()
			.failureRateThreshold(60)
			.slidingWindowSize(10)
			.waitDurationInOpenState(Duration.ofSeconds(60))
			.permittedNumberOfCallsInHalfOpenState(3)
			.recordExceptions(Exception.class)
			.build();
		return CircuitBreakerRegistry.of(java.util.Map.of("payu-gateway", config));
	}

	@Bean("payuCircuitBreaker")
	public CircuitBreaker payuCircuitBreaker(
			@org.springframework.beans.factory.annotation.Qualifier("payuCircuitBreakerRegistry")
			CircuitBreakerRegistry registry) {
		return registry.circuitBreaker("payu-gateway");
	}
}
