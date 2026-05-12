---
name: skill-resilience-pattern-enforcement
description: "Verify that all interactions with external services implement mandatory resilience patterns such as timeouts, circuit breakers, retries, and fallbacks."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# External Service Resilience Pattern Enforcement

## Objetivo
Verify that all interactions with external services implement mandatory resilience patterns such as timeouts, circuit breakers, retries, and fallbacks.

## Trigger
on code generation or modification of integration layers

## Inputs
- Java service classes interacting with external APIs
- Configuration files (e.g., application.yml, properties)
- Architecture diagrams

## Procedimiento
1. Identify all external service calls (e.g., ANI API, PayU SDK, DIAN proveedor tecnológico, AcquiringBank SFTP).
2. Confirm the presence and correct configuration of timeouts for each external call.
3. Check for the implementation of retry mechanisms with appropriate backoff strategies for transient failures.
4. Validate the use of circuit breakers (e.g., Resilience4j) to prevent cascading failures, with defined thresholds and open-state durations.
5. Ensure fallback mechanisms are in place for non-critical integrations to provide graceful degradation or cached responses.
6. For asynchronous calls, verify 'fire-and-forget' patterns are used where appropriate for non-critical operations.

## Output esperado
External service integrations demonstrate robust resilience patterns, configured to project standards.

## Source refs (project)
- robust-external-service-integration
- external-service-integration-resilience
- retry-pattern
- fallback-pattern
- HU-003
- Integraciones externas fijas: ANI (API REST propietaria, no existe OpenAPI spec, se integra vía adaptador con circuit breaker); Banco adquirente (SFTP + ISO 20022); PayU (SDK Java oficial)
