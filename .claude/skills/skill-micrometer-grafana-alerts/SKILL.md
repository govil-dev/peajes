---
name: skill-micrometer-grafana-alerts
description: "Instrument the application with Micrometer to expose key metrics, configure OpenTelemetry Collector for ingestion, and define Grafana alerts to proactively detect and notify on system degradations."
metadata:
  framework_principle: P2
  enforcement_mode: verify
  criticality_level: [standard]
---

# Implementing Micrometer Metrics and Grafana Alerting

## Objetivo
Instrument the application with Micrometer to expose key metrics, configure OpenTelemetry Collector for ingestion, and define Grafana alerts to proactively detect and notify on system degradations.

## Trigger
during development

## Inputs
- SLOs and error budgets
- Application code
- Grafana Cloud configuration

## Procedimiento
1. Identify critical metrics for each bounded context (e.g., `toll.authorization.latency`, `transaction.declined.reason`).
2. Use Micrometer to instrument code, exposing metrics like timers, counters, and gauges.
3. Configure OpenTelemetry Collector to scrape metrics and forward them to Grafana Cloud.
4. Define Grafana alert rules based on SLOs (e.g., p99 latency > 300ms, error rate > 1%).
5. Configure notification channels (PagerDuty, Slack) for alerts and resolutions.
6. Ensure alert messages include relevant context (service, metric value, affected entities) and exclude PII.
7. Test alert configurations by simulating failure conditions.

## Output esperado
A comprehensive observability setup that provides real-time insights into system health and triggers timely alerts for operational issues.

## Source refs (project)
- HU-012
- Logging: MDC obligatorio por request para correlationId, tagId (enmascarado), stationId, traceId.
