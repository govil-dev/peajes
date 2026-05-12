package co.quind.peajes.tollcollection.infrastructure.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;

@Configuration
public class Resilience4jConfig {

	@Bean
	public CircuitBreakerRegistry circuitBreakerRegistry() {
		CircuitBreakerConfig config = CircuitBreakerConfig.custom()
			.failureRateThreshold(50)
			.slidingWindowSize(20)
			.waitDurationInOpenState(Duration.ofSeconds(30))
			.permittedNumberOfCallsInHalfOpenState(5)
			.recordExceptions(Exception.class)
			.build();
		return CircuitBreakerRegistry.of(Map.of("account-management", config));
	}

	@Bean
	public CircuitBreaker accountManagementCircuitBreaker(CircuitBreakerRegistry registry) {
		return registry.circuitBreaker("account-management");
	}

}
