---
name: skill-spring-webflux-resilience
description: "Implement resilience patterns (timeouts, retries, circuit breakers, fallbacks) for external service calls using Spring WebFlux and Reactor to ensure system stability and responsiveness."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Applying Resilience Patterns in Spring WebFlux for External Integrations

## Objetivo
Implement resilience patterns (timeouts, retries, circuit breakers, fallbacks) for external service calls using Spring WebFlux and Reactor to ensure system stability and responsiveness.

## Trigger
during development

## Inputs
- External service API specifications
- Spring WebFlux application code
- Resilience4j configuration

## Procedimiento
1. Identify all external service integrations (e.g., ANI API, PayU SDK, DIAN provider).
2. For each integration, determine appropriate resilience strategies based on criticality and expected failure modes.
3. Implement timeouts using `timeout()` operators in Reactor streams to prevent long-running calls.
4. Apply retry logic with exponential backoff using `retryWhen()` for transient failures.
5. Integrate Circuit Breaker patterns (e.g., using Resilience4j) to prevent cascading failures to unresponsive services.
6. Define fallback mechanisms using `onErrorResume()` or `defaultIfEmpty()` for graceful degradation.
7. Ensure non-critical asynchronous calls use 'fire-and-forget' patterns where appropriate.
8. Write integration tests to simulate external service failures and verify resilience behavior.

## Output esperado
Robust external service integrations that gracefully handle failures, preventing system outages and maintaining user experience.

## Source refs (project)
- 1a573d41-f7e9-42c1-8693-3da003102e62
- fa28a475-5d69-4b7c-b9b0-09fd60a69654
- f014f09a-8fca-442a-ab78-b98d5c032581
- 9cb93807-f4b4-4c76-86c4-dade1c32d08e
- HU-003
- HU-005
- HU-007
- Integraciones externas fijas: ANI (API REST propietaria, no existe OpenAPI spec, se integra vía adaptador con circuit breaker); Banco adquirente (SFTP + ISO 20022); PayU (SDK Java oficial)
