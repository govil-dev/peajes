---
name: skill-implement-sensitive-data-masking
description: "Handle Personally Identifiable Information (PII) and sensitive financial data (e.g., PAN) according to data privacy laws (Ley 1581/2012) and PCI DSS L4 controls, including masking, tokenization, and strict avoidance of storage."
metadata:
  framework_principle: P4
  enforcement_mode: verify
  criticality_level: [standard]
---

# Implement Sensitive Data Masking and Tokenization

## Objetivo
Handle Personally Identifiable Information (PII) and sensitive financial data (e.g., PAN) according to data privacy laws (Ley 1581/2012) and PCI DSS L4 controls, including masking, tokenization, and strict avoidance of storage.

## Trigger
on handling any PII or sensitive financial data

## Inputs
- Data models with PII/sensitive fields
- Logging configurations
- External payment integrations (PayU)

## Procedimiento
1. Identify all fields containing PII or sensitive financial data.
2. For PANs, ensure tokenization is used (e.g., PayU token) and the full PAN is never stored or logged.
3. Implement masking functions (e.g., `toMasked()`) for PII before logging or displaying in non-secure contexts.
4. Ensure data in transit is protected (e.g., HTTPS/TLS 1.3 for card data).
5. Verify data at rest is encrypted (e.g., AES-256 via Cloud SQL encryption for PII).
6. Implement access controls and audit logging for sensitive data access.

## Output esperado
Sensitive data is protected throughout its lifecycle, with PII masked in logs, PANs tokenized, and data encrypted at rest and in transit.

## Source refs (project)
- c6de6d45-2b64-4524-b225-9bee71273e1b
- sensitive-data-handling-and-masking
- PCI DSS Nivel 4: PAN tokenizado; nunca se almacena el PAN completo; se guarda el token de PayU.
- PCI DSS Nivel 4: Transmisión de datos de tarjeta siempre vía HTTPS/TLS 1.3.
- Seguridad de datos: Ningún campo PII en logs; PAN tokenizado en tránsito y en reposo.
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- Ley 1581/2012 — Habeas data y protección de datos personales: PII clasificada como dato sensible; almacenamiento cifrado en reposo (AES-256 vía Cloud SQL encryption); logs no contienen PII (enmascaramiento); derecho de supresión (endpoint DELETE /api/v1/accounts/{{userId}} con anonimización); aviso de privacidad y consentimiento explícito; registro de tratamiento de datos ante SIC.
- PCI DSS Nivel 4: Aplica para procesamiento de números de tarjeta (recargas PayU); PAN tokenizado (nunca se almacena completo, solo token de PayU); transmisión de datos de tarjeta siempre vía HTTPS/TLS 1.3; acceso a datos de tarjeta restringido por roles (FinanceAnalyst con 2FA); escaneo de vulnerabilidades trimestral (ASV externo); log de auditoría para accesos a datos de pago, retención 1 año mínimo.
