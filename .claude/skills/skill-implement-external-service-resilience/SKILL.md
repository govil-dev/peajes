---
name: skill-implement-external-service-resilience
description: "Ensure robust and fault-tolerant interactions with external services by applying resilience patterns such as timeouts, circuit breakers, retries, and fallbacks."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Implement External Service Resilience Patterns

## Objetivo
Ensure robust and fault-tolerant interactions with external services by applying resilience patterns such as timeouts, circuit breakers, retries, and fallbacks.

## Trigger
on external service integration development

## Inputs
- External service integration points
- Latency and reliability requirements
- Resilience pattern configurations

## Procedimiento
1. Identify all external service calls within the component.
2. For each external call, determine appropriate resilience patterns (e.g., `Retry`, `Circuit Breaker`, `Timeout`, `Fallback`).
3. Implement the chosen patterns using Spring Boot's resilience capabilities or a dedicated library (e.g., Resilience4j).
4. Configure appropriate thresholds and policies for each pattern.
5. Write integration tests to verify the resilience patterns behave as expected under failure conditions.

## Output esperado
External service calls wrapped with configured resilience patterns, demonstrated by passing resilience tests.

## Source refs (project)
- 1a573d41-f7e9-42c1-8693-3da003102e62
- fa28a475-5d69-4b7c-b9b0-09fd60a69654
- f014f09a-8fca-442a-ab78-b98d5c032581
- 9cb93807-f4b4-4c76-86c4-dade1c32d08e
- Integraciones externas fijas: ANI (API REST propietaria, no existe OpenAPI spec, se integra vía adaptador con circuit breaker)
