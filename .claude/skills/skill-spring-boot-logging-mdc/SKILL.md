---
name: skill-spring-boot-logging-mdc
description: "Implement structured logging with MDC for correlation IDs and ensure PII is masked using `toMasked()` before logging, adhering to privacy standards."
metadata:
  framework_principle: P4
  enforcement_mode: block
  criticality_level: [standard]
---

# Spring Boot Logging with MDC and PII Masking

## Objetivo
Implement structured logging with MDC for correlation IDs and ensure PII is masked using `toMasked()` before logging, adhering to privacy standards.

## Trigger
on-code-change

## Inputs
- Java source code (logging calls)
- Logback/Log4j2 configuration files

## Procedimiento
1. Verify that MDC (Mapped Diagnostic Context) is used to include `correlationId`, `tagId` (masked), `stationId`, and `traceId` in all log entries.
2. Inspect log statements to ensure no PII is directly logged.
3. Confirm that `toMasked()` methods are consistently applied to PII fields before they are passed to logging frameworks.
4. Review logging configurations to prevent accidental PII exposure.

## Output esperado
Logs are structured, traceable, and free of PII, complying with privacy and security requirements.

## Source refs (project)
- Logging: MDC obligatorio por request para correlationId, tagId (enmascarado), stationId, traceId.
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- Seguridad de datos: Ningún campo PII en logs; PAN tokenizado en tránsito y en reposo.
