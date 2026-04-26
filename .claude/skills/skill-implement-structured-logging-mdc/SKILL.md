---
name: skill-implement-structured-logging-mdc
description: "Ensure all log entries include mandatory MDC fields (correlationId, tagId, stationId, traceId) for traceability and strictly prohibit PII by using masking functions."
metadata:
  framework_principle: P4
  enforcement_mode: verify
  criticality_level: [standard]
---

# Implement Structured Logging with MDC and PII Masking

## Objetivo
Ensure all log entries include mandatory MDC fields (correlationId, tagId, stationId, traceId) for traceability and strictly prohibit PII by using masking functions.

## Trigger
on implementing new service endpoints or event handlers

## Inputs
- Logging configuration files
- Request/event processing entry points
- Data models with PII

## Procedimiento
1. Configure logging frameworks (e.g., Logback with SLF4J) to automatically include MDC context.
2. Implement a mechanism to populate `correlationId`, `tagId` (masked), `stationId`, and `traceId` in the MDC at the start of each request/event processing flow.
3. Develop or utilize `toMasked()` utility methods for all PII fields before they are passed to logging statements.
4. Conduct regular log reviews to ensure no PII is inadvertently logged.

## Output esperado
Comprehensive, traceable, and PII-free log records that facilitate debugging and auditing without compromising privacy.

## Source refs (project)
- Logging: MDC obligatorio por request para correlationId, tagId (enmascarado), stationId, traceId.
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- Seguridad de datos: Ningún campo PII en logs; PAN tokenizado en tránsito y en reposo.
- Ley 1581/2012 — Habeas data y protección de datos personales: PII clasificada como dato sensible; logs no contienen PII (enmascaramiento);
