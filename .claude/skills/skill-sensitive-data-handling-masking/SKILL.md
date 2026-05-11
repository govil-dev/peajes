---
name: skill-sensitive-data-handling-masking
description: "Validate that Personally Identifiable Information (PII) and sensitive financial data are handled according to Ley 1581/2012 and PCI DSS L4 controls, including masking, tokenization, and restricted logging."
metadata:
  framework_principle: P4
  enforcement_mode: block
  criticality_level: [standard]
---

# Sensitive Data Handling and PII Masking

## Objetivo
Validate that Personally Identifiable Information (PII) and sensitive financial data are handled according to Ley 1581/2012 and PCI DSS L4 controls, including masking, tokenization, and restricted logging.

## Trigger
on code generation or modification involving PII or sensitive financial data

## Inputs
- Java code handling PII/sensitive data
- Logging configurations
- API contracts
- Security configurations (Spring Security)
- Database schema

## Procedimiento
1. Identify all data fields classified as PII (e.g., license plates) or sensitive financial data (e.g., PAN).
2. Verify that PII is masked using `toMasked()` before being included in logs, alerts, or non-secure event payloads.
3. Confirm that Primary Account Numbers (PAN) are tokenized (e.g., via PayU) and never stored or transmitted in full within the system.
4. Check that data transmission for sensitive data always uses HTTPS/TLS 1.3.
5. Ensure access to sensitive data is restricted by roles and requires multi-factor authentication (2FA) for privileged users.
6. Review data retention policies for sensitive data, ensuring compliance with legal and internal requirements.

## Output esperado
PII and sensitive financial data are securely handled, masked, or tokenized, and access is appropriately restricted.

## Source refs (project)
- sensitive-data-handling-and-masking
- c6de6d45-2b64-4524-b225-9bee71273e1b
- Ley 1581/2012 — Habeas data y protección de datos personales
- PCI DSS Nivel 4
- Seguridad de datos: Ningún campo PII en logs; PAN tokenizado en tránsito y en reposo.
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- HU-003
- HU-012
