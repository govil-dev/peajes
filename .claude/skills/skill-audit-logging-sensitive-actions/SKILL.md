---
name: skill-audit-logging-sensitive-actions
description: "Ensure all critical actions, especially those involving sensitive data or administrative changes, are properly audited with sufficient detail and retained for compliance."
metadata:
  framework_principle: P4
  enforcement_mode: verify
  criticality_level: [standard]
---

# Audit Logging for Critical and Sensitive Actions

## Objetivo
Ensure all critical actions, especially those involving sensitive data or administrative changes, are properly audited with sufficient detail and retained for compliance.

## Trigger
on code generation or modification of critical business logic or administrative features

## Inputs
- Java code for critical business operations
- Logging configurations
- Database schema for audit trails

## Procedimiento
1. Identify all operations that modify sensitive data, administrative configurations, or financial records (e.g., `ManualOverride`, `ReconciliationItem` resolution, `Dispute` resolution).
2. Verify that these actions generate audit logs containing essential details like `operatorId`, `timestamp`, `actionType`, `affectedEntityId`, and relevant parameters.
3. Ensure that audit logs are immutable and retained for the minimum required period (e.g., 1 year for PCI DSS, 7 years for DIAN-related records).
4. Confirm that PII is masked in audit logs to comply with privacy regulations.

## Output esperado
Critical and sensitive actions are comprehensively audited, providing an immutable record for compliance and forensic analysis.

## Source refs (project)
- PCI DSS Nivel 4: Log de auditoría para accesos a datos de pago, retención 1 año mínimo.
- Logging: MDC obligatorio por request para correlationId, tagId (enmascarado), stationId, traceId.
- HU-002
- HU-008
- Resolución DIAN 000042/2020 — Facturación electrónica
