---
name: skill-dian-e-invoicing-compliance
description: "Ensure the electronic invoicing process (generation, signing, CUFE, retention) fully complies with Colombian DIAN Resolution 000042/2020."
metadata:
  framework_principle: P1
  enforcement_mode: verify
  criticality_level: [standard]
---

# DIAN Electronic Invoicing Compliance

## Objetivo
Ensure the electronic invoicing process (generation, signing, CUFE, retention) fully complies with Colombian DIAN Resolution 000042/2020.

## Trigger
on code generation or modification of billing services

## Inputs
- Billing service code (invoice generation logic)
- Configuration for DIAN technological provider integration
- Database schema for invoice storage

## Procedimiento
1. Verify that invoices are generated in XML UBL 2.1 format and digitally signed with a DIAN-authorized certificate.
2. Confirm the system integrates with an authorized technological provider to obtain the CUFE (Código Único de Facturación Electrónica).
3. Ensure that credit notes for cancellations or corrections are also generated electronically with their own CUFE.
4. Validate that signed XML invoices are retained for a minimum of 7 years (project policy exceeds 5-year legal minimum).
5. Check for idempotency in invoice generation to prevent duplicate invoices for the same transaction.

## Output esperado
Electronic invoicing process is fully compliant with DIAN regulations, generating valid, signed invoices and credit notes.

## Source refs (project)
- Resolución DIAN 000042/2020 — Facturación electrónica
- HU-005
- HU-006
