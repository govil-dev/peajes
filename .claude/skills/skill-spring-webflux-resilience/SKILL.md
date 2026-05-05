---
name: skill-spring-webflux-resilience
description: "Ensure external service integrations are robust against transient failures by applying appropriate resilience patterns (timeouts, circuit breakers, retries, fallbacks) compatible with Spring WebFlux's reactive nature."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Implement Spring WebFlux Resilience Patterns

## Objetivo
Ensure external service integrations are robust against transient failures by applying appropriate resilience patterns (timeouts, circuit breakers, retries, fallbacks) compatible with Spring WebFlux's reactive nature.

## Trigger
on code change in external integration modules

## Inputs
- Service client code for external integrations
- Resilience configuration properties

## Procedimiento
1. Identify all interactions with external services (e.g., ANI API, PayU, DIAN provider).
2. Apply Resilience4j or similar libraries to implement `Timeout`, `Retry`, `CircuitBreaker`, and `Fallback` patterns.
3. Verify that resilience mechanisms are integrated in a non-blocking manner, consistent with Spring WebFlux.
4. Ensure configuration parameters (e.g., `failureRateThreshold`, `waitDurationInOpenState`, retry attempts) align with business rules and SLOs.

## Output esperado
External service calls are wrapped with resilience patterns, configured according to project standards, and do not block reactive flows.

## Source refs (project)
- 1a573d41-f7e9-42c1-8693-3da003102e62
- fa28a475-5d69-4b7c-b9b0-09fd60a69654
- f014f09a-8fca-442a-ab78-b98d5c032581
- 9cb93807-f4b4-4c76-86c4-dade1c32d08e
- HU-003:business_rules:4
- HU-003:business_rules:5
- HU-001:exceptions:5
