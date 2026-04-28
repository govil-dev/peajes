---
name: skill-pii-masking-logging-java
description: "Ensure Personally Identifiable Information (PII) and sensitive financial data are never exposed in logs or stored insecurely, adhering to privacy regulations and PCI DSS."
metadata:
  framework_principle: P4
  enforcement_mode: verify
  criticality_level: [standard]
---

# PII Masking and Secure Logging in Java

## Objetivo
Ensure Personally Identifiable Information (PII) and sensitive financial data are never exposed in logs or stored insecurely, adhering to privacy regulations and PCI DSS.

## Trigger
during development

## Inputs
- Data models
- Logging configurations
- Security requirements

## Procedimiento
1. Identify all data fields classified as PII or sensitive financial data (e.g., PAN, license plates).
2. Implement `toMasked()` methods for all PII-containing Value Objects or DTOs before logging or transmitting.
3. Configure logging frameworks (e.g., Logback) to prevent accidental PII leakage, potentially using custom appenders or filters.
4. Verify that PII is tokenized or masked in transit and at rest, especially for payment card data.
5. Conduct automated tests to assert that sensitive data does not appear in log outputs.

## Output esperado
Code that correctly masks PII in logs and handles sensitive data according to privacy standards, verified by automated tests.

## Source refs (project)
- c6de6d45-2b64-4524-b225-9bee71273e1b
- Ley 1581/2012 — Habeas data y protección de datos personales
- PCI DSS Nivel 4
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
