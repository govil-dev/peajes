---
name: skill-webflux-resilience-patterns
description: "Ensure all external service integrations within Spring WebFlux applications correctly implement resilience patterns such as timeouts, circuit breakers, retries, and fallbacks to maintain system stability and responsiveness."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Spring WebFlux Resilience Pattern Implementation

## Objetivo
Ensure all external service integrations within Spring WebFlux applications correctly implement resilience patterns such as timeouts, circuit breakers, retries, and fallbacks to maintain system stability and responsiveness.

## Trigger
on code review

## Inputs
- Source code of external service integrations
- Resilience configuration files
- Architectural diagrams showing external dependencies

## Procedimiento
1. Identify all external service calls within the Spring WebFlux application.
2. Verify that each external call is wrapped with appropriate resilience mechanisms (e.g., Reactor's `timeout`, Resilience4j `CircuitBreaker`, `Retry` operators).
3. Confirm that fallback mechanisms are provided for non-critical operations or when external services are unavailable.
4. Review configuration parameters for resilience patterns (e.g., timeout durations, retry attempts, circuit breaker thresholds) against business requirements and SLOs.
5. Check for proper error handling and logging when resilience patterns are triggered.

## Output esperado
External service integrations demonstrating correct application and configuration of resilience patterns, with clear error handling.

## Source refs (project)
- external-service-integration-resilience
- robust-external-service-integration
- retry-pattern
- fallback-pattern
- HU-003
- HU-001
