---
name: skill-structured-mdc-logging
description: "Ensure all services implement structured logging, utilizing MDC (Mapped Diagnostic Context) for mandatory correlation IDs and masking PII, to provide clear, searchable, and privacy-compliant operational insights."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Implement Structured Logging with MDC

## Objetivo
Ensure all services implement structured logging, utilizing MDC (Mapped Diagnostic Context) for mandatory correlation IDs and masking PII, to provide clear, searchable, and privacy-compliant operational insights.

## Trigger
on code change in logging configuration or new service implementation

## Inputs
- Logging configuration files (logback.xml)
- Service code with logging statements
- PII masking utility

## Procedimiento
1. Configure SLF4J/Logback to use MDC for `correlationId`, `tagId` (masked), `stationId`, and `traceId` for every request/event.
2. Implement a `toMasked()` utility method for all PII fields and ensure it's used before logging any sensitive data.
3. Ensure log messages are structured (e.g., JSON format) for easy parsing and analysis by logging tools.
4. Verify that no PII or sensitive data is accidentally included in logs or alert messages.

## Output esperado
Logs are structured, contain mandatory MDC context, and are free of PII, enabling effective debugging and monitoring.

## Source refs (project)
- Logging: MDC obligatorio por request para correlationId, tagId (enmascarado), stationId, traceId.
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- HU-012:business_rules:12
