---
name: skill-reactive-resilience-implementation
description: "Ensure all external service calls within Spring WebFlux applications implement robust resilience patterns such as timeouts, circuit breakers, retries, and fallbacks to handle transient failures and maintain system stability."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Reactive Resilience Implementation

## Objetivo
Ensure all external service calls within Spring WebFlux applications implement robust resilience patterns such as timeouts, circuit breakers, retries, and fallbacks to handle transient failures and maintain system stability.

## Trigger
on code generation or modification of external service clients

## Inputs
- External service integration code
- Service client configurations
- Business requirements for external call reliability

## Procedimiento
1. Identify all external service integration points in the Spring WebFlux application.
2. Apply appropriate resilience patterns (e.g., Reactor's `timeout`, Resilience4j `CircuitBreaker`, `Retry`) based on the criticality and expected behavior of the external service.
3. Configure specific timeouts for each external call, as per business rules (e.g., 3 seconds for PayU).
4. Implement fallback mechanisms to provide graceful degradation or cached responses when external services are unavailable.
5. Ensure non-critical asynchronous calls use fire-and-forget patterns where appropriate.

## Output esperado
Code demonstrating configured resilience patterns for external service calls, with appropriate error handling and fallbacks.

## Source refs (project)
- external-service-integration-resilience
- robust-external-service-integration
- retry-pattern
- fallback-pattern
- HU-003
- HU-007
- HU-010
