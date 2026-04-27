---
name: skill-external-service-resilience
description: "Implement resilience patterns such as timeouts, circuit breakers, retries, and fallbacks for all external service integrations (e.g., ANI API, PayU) to ensure system stability and graceful degradation."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# External Service Resilience Implementation

## Objetivo
Implement resilience patterns such as timeouts, circuit breakers, retries, and fallbacks for all external service integrations (e.g., ANI API, PayU) to ensure system stability and graceful degradation.

## Trigger
during development

## Inputs
- external service integration points
- resilience requirements
- service level objectives

## Procedimiento
1. Identify external service calls and their criticality.
2. Apply appropriate resilience patterns (e.g., Resilience4j for circuit breakers, Spring Retry for retries).
3. Configure timeouts for all external calls to prevent indefinite waits.
4. Implement fallback mechanisms for non-critical operations or when external services are unavailable.
5. Document resilience configurations and testing strategies.

## Output esperado
External service integrations with implemented and tested resilience patterns.

## Source refs (project)
- 1a573d41-f7e9-42c1-8693-3da003102e62
- fa28a475-5d69-4b7c-b9b0-09fd60a69654
- f014f09a-8fca-442a-ab78-b98d5c032581
- 9cb93807-f4b4-4c76-86c4-dade1c32d08e
