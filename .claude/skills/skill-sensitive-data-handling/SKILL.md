---
name: skill-sensitive-data-handling
description: "Implement comprehensive data privacy controls, including masking, tokenization, encryption, and strict access restrictions for PII and sensitive financial data (PAN), to comply with Ley 1581/2012 and PCI DSS L4 requirements."
metadata:
  framework_principle: P4
  enforcement_mode: block
  criticality_level: [standard]
---

# Secure Sensitive Data Handling (PII & PCI DSS)

## Objetivo
Implement comprehensive data privacy controls, including masking, tokenization, encryption, and strict access restrictions for PII and sensitive financial data (PAN), to comply with Ley 1581/2012 and PCI DSS L4 requirements.

## Trigger
during development

## Inputs
- data models
- privacy requirements (Ley 1581/2012)
- security requirements (PCI DSS L4)

## Procedimiento
1. Identify all PII and sensitive financial data fields.
2. Implement tokenization for PAN (e.g., via PayU SDK) and ensure full PAN is never stored.
3. Apply masking for PII in logs and user interfaces.
4. Ensure data at rest is encrypted (e.g., AES-256 for Cloud SQL).
5. Enforce HTTPS/TLS 1.3 for all data in transit, especially card data.
6. Implement role-based access control with 2FA for sensitive data access.
7. Develop data suppression/anonimization endpoints for user data deletion requests.

## Output esperado
System components that securely handle sensitive data, with auditable compliance to privacy and security standards.

## Source refs (project)
- c6de6d45-2b64-4524-b225-9bee71273e1b
- Ley 1581/2012 — Habeas data y protección de datos personales
- PCI DSS Nivel 4
