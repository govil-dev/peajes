---
name: skill-pii-masking-pci-dss
description: "Ensure Personally Identifiable Information (PII) and sensitive financial data are handled with minimal disclosure, including masking in logs, tokenization, and adherence to PCI DSS L4 controls and Ley 1581/2012."
metadata:
  framework_principle: P4
  enforcement_mode: block
  criticality_level: [standard]
---

# PII Masking and PCI DSS L4 Compliance

## Objetivo
Ensure Personally Identifiable Information (PII) and sensitive financial data are handled with minimal disclosure, including masking in logs, tokenization, and adherence to PCI DSS L4 controls and Ley 1581/2012.

## Trigger
on-code-change

## Inputs
- Java source code
- Logging configurations
- Security configurations
- Database schemas

## Procedimiento
1. Identify all data fields classified as PII or sensitive financial data (e.g., PAN, license plates).
2. Verify that PII is never stored in logs and is masked using `toMasked()` methods before logging.
3. Confirm that PANs are tokenized and never stored in full, only PayU tokens are retained.
4. Ensure data transmission involving sensitive data uses HTTPS/TLS 1.3.
5. Check for role-based access restrictions to sensitive data, requiring 2FA for privileged roles.
6. Verify implementation of data suppression endpoints for Ley 1581/2012 compliance.

## Output esperado
Sensitive data is protected according to privacy laws and PCI DSS, minimizing disclosure risks.

## Source refs (project)
- sensitive-data-handling-and-masking
- c6de6d45-2b64-4524-b225-9bee71273e1b
- PCI DSS Nivel 4
- Ley 1581/2012 — Habeas data y protección de datos personales
- Seguridad de datos: Ningún campo PII en logs; PAN tokenizado en tránsito y en reposo.
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- HU-003
