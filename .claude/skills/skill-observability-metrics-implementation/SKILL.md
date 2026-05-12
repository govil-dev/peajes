---
name: skill-observability-metrics-implementation
description: "Verify that critical business operations and system interactions emit required metrics with appropriate tags for effective monitoring, alerting, and performance analysis."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Observability Metrics Implementation

## Objetivo
Verify that critical business operations and system interactions emit required metrics with appropriate tags for effective monitoring, alerting, and performance analysis.

## Trigger
on code change

## Inputs
- Java source code
- Monitoring configurations
- Use case business rules

## Procedimiento
1. Identify key business operations and integration points that require metrics (e.g., `toll.manual_overrides.count`).
2. Check for the presence of metric instrumentation using a compatible library (e.g., Micrometer for Spring Boot).
3. Validate that metrics include relevant tags (e.g., `stationId`, `reason`) to enable granular analysis and filtering.
4. Ensure metrics are exposed in a format consumable by the monitoring system (e.g., Prometheus, Stackdriver).
5. Review alert configurations to confirm they leverage emitted metrics effectively (e.g., alerts for override rate exceeding 5%).

## Output esperado
Assessment of metric coverage and correctness, identifying gaps or misconfigurations.

## Source refs (project)
- use_case:a08d2aa3-7139-4b9b-8347-5c0d3640c290
