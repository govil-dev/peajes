---
name: skill-logging-observability-standards
description: "Validate that logging adheres to mandatory standards, including MDC for correlation, PII masking, and proper metric publication for observability and alerting."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Logging and Observability Standards Compliance

## Objetivo
Validate that logging adheres to mandatory standards, including MDC for correlation, PII masking, and proper metric publication for observability and alerting.

## Trigger
on code generation or modification of logging or metric instrumentation

## Inputs
- Logging configurations (e.g., Logback.xml)
- Java code with logging statements
- Micrometer/OpenTelemetry instrumentation code
- Grafana alert definitions

## Procedimiento
1. Verify that all requests and critical operations use MDC (Mapped Diagnostic Context) to include `correlationId`, `tagId` (masked), `stationId`, and `traceId`.
2. Confirm that no PII is ever logged in plain text; ensure `toMasked()` or equivalent methods are used for sensitive data before logging.
3. Check that key business metrics (e.g., `toll.authorization.latency`, `TransactionDeclined` reasons) are published using Micrometer and OpenTelemetry Collector.
4. Review alert configurations to ensure they correctly monitor SLOs and error rates, and that alert messages exclude PII.

## Output esperado
Logs are standardized, PII-free, and metrics are correctly published to support robust observability and alerting.

## Source refs (project)
- Logging: MDC obligatorio por request para correlationId, tagId (enmascarado), stationId, traceId.
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- HU-012
