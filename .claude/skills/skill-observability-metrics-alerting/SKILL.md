---
name: skill-observability-metrics-alerting
description: "Instrument applications with Micrometer, configure OpenTelemetry Collector, and define Grafana alerts for key Service Level Objectives (SLOs) like latency and error rates, integrating with PagerDuty and Slack for notifications."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Observability Metrics and Alerting Setup

## Objetivo
Instrument applications with Micrometer, configure OpenTelemetry Collector, and define Grafana alerts for key Service Level Objectives (SLOs) like latency and error rates, integrating with PagerDuty and Slack for notifications.

## Trigger
on new service deployment or critical feature implementation

## Inputs
- Application code (for instrumentation)
- OpenTelemetry Collector configuration
- Grafana alert definitions
- PagerDuty and Slack integration details

## Procedimiento
1. Integrate Micrometer into Spring Boot applications to expose custom metrics (e.g., `toll.authorization.latency`).
2. Configure OpenTelemetry Collector to scrape metrics and send them to Grafana Cloud.
3. Define Grafana alert rules for SLO violations (e.g., p99 latency > 300ms for 5 minutes, error rate > 1%).
4. Configure alert notifications to PagerDuty (with severity levels) and Slack.
5. Ensure alert messages include relevant context (service, current value, affected stations) and exclude PII.

## Output esperado
Instrumented code, active monitoring dashboards, and functional alerts for SLO violations.

## Source refs (project)
- HU-012
- gov-score-30d
- gov-pass-rate-30d
