---
name: skill-observability-slo-alerts
description: "Ensure that critical system metrics are correctly published, monitored, and that automated alerts are configured to notify OpsAdmins of SLO violations (e.g., latency, error rates), with strict exclusion of PII from alert messages."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Observability Alerting Configuration for SLOs

## Objetivo
Ensure that critical system metrics are correctly published, monitored, and that automated alerts are configured to notify OpsAdmins of SLO violations (e.g., latency, error rates), with strict exclusion of PII from alert messages.

## Trigger
on deployment

## Inputs
- Monitoring system configurations (Grafana, PagerDuty)
- Application code publishing metrics (Micrometer, OpenTelemetry)
- Alert rule definitions
- Logging configurations

## Procedimiento
1. Verify that `toll.authorization.latency` (p99) and `TransactionDeclined` rates are published using Micrometer and OpenTelemetry Collector.
2. Review Grafana alert rules to confirm they accurately reflect SLOs (e.g., p99 < 300ms for latency, <1% error rate).
3. Check alert notification channels (PagerDuty, Slack) and severity levels (HIGH, CRITICAL) are correctly configured.
4. Examine alert message templates to ensure no PII is included, and `toMasked()` is used for any potentially sensitive data.
5. Validate alert resolution conditions (e.g., latency returning to < 200ms) for automatic closure.

## Output esperado
A robust observability setup with accurate SLO monitoring and automated, PII-compliant alerts for operational incidents.

## Source refs (project)
- HU-012
- gov-score-30d
- gov-pass-rate-30d
- Logging
