---
name: skill-pii-pan-data-privacy
description: "Ensure Personally Identifiable Information (PII) and sensitive financial data (PAN) are handled in strict compliance with Ley 1581/2012 and PCI DSS Level 4, including masking, tokenization, encryption, and restricted access."
metadata:
  framework_principle: P4
  enforcement_mode: verify
  criticality_level: [standard]
---

# Secure PII and PAN Data Handling

## Objetivo
Ensure Personally Identifiable Information (PII) and sensitive financial data (PAN) are handled in strict compliance with Ley 1581/2012 and PCI DSS Level 4, including masking, tokenization, encryption, and restricted access.

## Trigger
on code change involving PII or PAN data

## Inputs
- Data models
- Logging configurations
- API endpoints
- Database schemas

## Procedimiento
1. Identify all PII and PAN fields across the system (in transit, at rest, in logs).
2. Implement data masking (`toMasked()` utility) for all PII before logging or displaying in non-privileged contexts.
3. Ensure PANs are tokenized immediately upon receipt (e.g., via PayU SDK) and never stored in full.
4. Verify data at rest is encrypted (e.g., Cloud SQL encryption) and data in transit uses HTTPS/TLS 1.3.
5. Implement role-based access control for sensitive data, requiring 2FA for privileged roles.
6. Ensure mechanisms for data suppression (right to be forgotten) are in place.

## Output esperado
PII and PAN data are protected through masking, tokenization, encryption, and access controls, meeting regulatory compliance.

## Source refs (project)
- c6de6d45-2b64-4524-b225-9bee71273e1b
- Ley 1581/2012 — Habeas data y protección de datos personales
- PCI DSS Nivel 4
- Seguridad de datos: Ningún campo PII en logs; PAN tokenizado en tránsito y en reposo.
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- HU-003:business_rules:6
- HU-009:business_rules:9
- HU-012:business_rules:12
