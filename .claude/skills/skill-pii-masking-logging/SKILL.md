---
name: skill-pii-masking-logging
description: "Ensure that Personally Identifiable Information (PII) and sensitive financial data are never exposed in logs, audit trails, or non-secure communication channels, by enforcing masking, tokenization, or strict avoidance of storage."
metadata:
  framework_principle: P4
  enforcement_mode: verify
  criticality_level: [standard]
---

# PII Masking and Logging Compliance

## Objetivo
Ensure that Personally Identifiable Information (PII) and sensitive financial data are never exposed in logs, audit trails, or non-secure communication channels, by enforcing masking, tokenization, or strict avoidance of storage.

## Trigger
on code review

## Inputs
- Application logs
- Source code handling PII (e.g., DTOs, mappers, logging statements)
- Security configurations
- Kafka event schemas

## Procedimiento
1. Identify all data fields classified as PII or sensitive financial data (e.g., PAN, license plates, account IDs).
2. Verify that `toMasked()` methods or similar mechanisms are applied to PII fields before logging.
3. Confirm that PANs are tokenized and never stored in full, adhering to PCI DSS L4 requirements.
4. Review log configurations to ensure PII filtering is active and effective.
5. Check event payloads for any accidental inclusion of unmasked PII.
6. Validate that data transmission for sensitive information uses secure protocols (e.g., HTTPS/TLS 1.3).

## Output esperado
Logs and system outputs free of unmasked PII, with sensitive data handled according to privacy and security standards.

## Source refs (project)
- sensitive-data-handling-and-masking
- Ley 1581/2012 — Habeas data y protección de datos personales
- PCI DSS Nivel 4
- Seguridad de datos
- Logging
- HU-002
- HU-012
