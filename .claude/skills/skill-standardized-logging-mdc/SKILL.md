---
name: skill-standardized-logging-mdc
description: "Implement standardized logging practices using MDC (Mapped Diagnostic Context) for correlation IDs (`correlationId`, `traceId`, `tagId`, `stationId`) and ensure PII is strictly prohibited from logs, using `toMasked()` methods for sensitive data."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Standardized Logging with MDC and PII Masking

## Objetivo
Implement standardized logging practices using MDC (Mapped Diagnostic Context) for correlation IDs (`correlationId`, `traceId`, `tagId`, `stationId`) and ensure PII is strictly prohibited from logs, using `toMasked()` methods for sensitive data.

## Trigger
during development

## Inputs
- logging configuration
- data models with PII
- compliance requirements for logging

## Procedimiento
1. Configure logging frameworks (e.g., Logback) to include MDC values in log output.
2. Implement MDC population at the entry point of each request/message processing flow.
3. Develop `toMasked()` methods for all data objects containing PII or sensitive information.
4. Conduct code reviews to verify no PII is directly logged without masking.
5. Ensure log retention policies align with compliance requirements (e.g., 1 year for payment data access logs).

## Output esperado
Application logs that are easily traceable, contain necessary context, and are free of PII.

## Source refs (project)
- Logging: MDC obligatorio por request para correlationId, tagId (enmascarado), stationId, traceId.
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
