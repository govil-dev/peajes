---
name: skill-external-service-resilience-patterns
description: "Verify that integrations with external services (e.g., PayU) implement robust resilience patterns such as timeouts, retries with backoff, and circuit breakers to handle failures gracefully and prevent cascading failures."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# External Service Resilience Patterns Implementation

## Objetivo
Verify that integrations with external services (e.g., PayU) implement robust resilience patterns such as timeouts, retries with backoff, and circuit breakers to handle failures gracefully and prevent cascading failures.

## Trigger
on code change

## Inputs
- Java source code for external integrations
- Configuration files (e.g., application.yml)

## Procedimiento
1. Examine code for integration points with external services (e.g., PayU SDK).
2. Confirm the presence and configuration of timeouts for all external calls (e.g., 3 seconds for PayU).
3. Validate the implementation of retry mechanisms with appropriate backoff strategies (e.g., one retry with 500ms backoff).
4. Check for circuit breaker patterns (e.g., Resilience4j) with configured failure rate thresholds (e.g., 60%) and open state durations (e.g., 60 seconds).
5. Assess error handling for external service failures to ensure proper fallback mechanisms.

## Output esperado
Assessment of resilience pattern implementation, highlighting missing or misconfigured patterns.

## Source refs (project)
- use_case:fe4645b8-0c14-47ef-adb1-970bfce30bf7
