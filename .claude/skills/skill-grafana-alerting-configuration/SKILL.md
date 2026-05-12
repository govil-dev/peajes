---
name: skill-grafana-alerting-configuration
description: "Configure and verify monitoring metrics (Micrometer, OpenTelemetry) and Grafana alerting rules for critical SLOs (e.g., latency, error rates), ensuring timely notifications to OpsAdmin."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Grafana Alerting and Observability Configuration

## Objetivo
Configure and verify monitoring metrics (Micrometer, OpenTelemetry) and Grafana alerting rules for critical SLOs (e.g., latency, error rates), ensuring timely notifications to OpsAdmin.

## Trigger
on-config-change

## Inputs
- Java source code (Micrometer instrumentation)
- OpenTelemetry Collector configuration
- Grafana dashboard and alerting configurations

## Procedimiento
1. Verify that `toll.authorization.latency` (p99) and `TransactionDeclined` rates are correctly published via Micrometer/OpenTelemetry.
2. Check Grafana for correctly configured alerting rules for latency (p99 > 300ms for 5 min) and error rates (>1% SYSTEM_ERROR for 5 min).
3. Ensure alerts are routed to PagerDuty and Slack with appropriate severity levels (HIGH, CRITICAL).
4. Confirm alert messages include relevant context (service, current value, affected stations).
5. Verify auto-resolution logic for alerts when conditions normalize.

## Output esperado
The system provides comprehensive observability and proactive alerting for critical performance and error conditions.

## Source refs (project)
- HU-012
- gov-score-30d
- gov-pass-rate-30d
