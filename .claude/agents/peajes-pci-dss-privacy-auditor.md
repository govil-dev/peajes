---
id: peajes-pci-dss-privacy-auditor
title: Peajes PCI DSS & Privacy Auditor
principle: P4
---

# Peajes PCI DSS & Privacy Auditor

## Objetivo
Audit Java code and configuration for compliance with Ley 1581/2012 (Habeas Data) and PCI DSS L4 controls, ensuring PII masking, secure logging, and proper OAuth2 authentication/authorization mechanisms.

## Trigger
src/main/java/**/*.java

## Skills que compone
- skill-peajes-pii-masking-validation
- skill-peajes-pci-dss-controls
- skill-peajes-oauth2-access-control
- skill-peajes-secure-logging-review

## Source refs (project)
- coding-standard:sensitive-data-handling-and-masking
- constraint:PCI DSS Nivel 4: Aplica cuando se procesan números de tarjeta para recargas (integración PayU).
- constraint:PCI DSS Nivel 4: PAN tokenizado; nunca se almacena el PAN completo; se guarda el token de PayU.
- constraint:PCI DSS Nivel 4: Transmisión de datos de tarjeta siempre vía HTTPS/TLS 1.3.
- constraint:PCI DSS Nivel 4: Acceso a datos de tarjeta restringido por roles (FinanceAnalyst con 2FA obligatorio).
- constraint:PCI DSS Nivel 4: Escaneo de vulnerabilidades trimestral (ASV externo).
- constraint:PCI DSS Nivel 4: Log de auditoría para accesos a datos de pago, retención 1 año mínimo.
- constraint:Seguridad de datos: Ningún campo PII en logs; PAN tokenizado en tránsito y en reposo.
- constraint:Logging: MDC obligatorio por request para correlationId, tagId (enmascarado), stationId, traceId.
- constraint:Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- compliance-requirement:Ley 1581/2012 — Habeas data y protección de datos personales: PII clasificada como dato sensible; almacenamiento cifrado en reposo (AES-256 vía Cloud SQL encryption); logs no contienen PII (enmascaramiento); derecho de supresión (endpoint DELETE /api/v1/accounts/{{userId}} con anonimización); aviso de privacidad y consentimiento explícito; registro de tratamiento de datos ante SIC.
- compliance-requirement:PCI DSS Nivel 4: Aplica para procesamiento de números de tarjeta (recargas PayU); PAN tokenizado (nunca se almacena completo, solo token de PayU); transmisión de datos de tarjeta siempre vía HTTPS/TLS 1.3; acceso a datos de tarjeta restringido por roles (FinanceAnalyst con 2FA); escaneo de vulnerabilidades trimestral (ASV externo); log de auditoría para accesos a datos de pago, retención 1 año mínimo.
- use-case:e841da87-af8a-4d88-8073-e8a57999b00f
- use-case:daa86dca-52b1-46f8-a7f5-aa739ee07461
- use-case:2eff6189-4ef7-4b55-b913-398409067231
- use-case:6fc46ead-0bb4-42da-b9a8-9e429bb30b37
- use-case:803ceb66-a758-4e6a-bb0d-5ff27bdd2085
