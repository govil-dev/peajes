---
name: skill-spring-webflux-resilience
description: "Implement resilience patterns such as timeouts, circuit breakers, retries, and fallbacks for external service integrations using Spring WebFlux and Reactor capabilities."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Spring WebFlux Resilience Patterns

## Objetivo
Implement resilience patterns such as timeouts, circuit breakers, retries, and fallbacks for external service integrations using Spring WebFlux and Reactor capabilities.

## Trigger
on-code-change

## Inputs
- Java source code (Spring WebFlux services)
- External API client configurations

## Procedimiento
1. Identify all external service calls and ensure they are wrapped with resilience patterns.
2. Verify that timeouts are configured for all blocking or potentially slow external calls.
3. Check for the presence of Circuit Breaker implementations (e.g., Resilience4j) for critical external dependencies.
4. Ensure retry mechanisms are in place for transient failures, with appropriate backoff strategies.
5. Confirm fallback mechanisms are provided for non-critical operations when external services are unavailable.

## Output esperado
External service integrations are robust against failures and latency, maintaining system stability.

## Source refs (project)
- robust-external-service-integration
- external-service-integration-resilience
- fallback-pattern
- retry-pattern
- HU-003
