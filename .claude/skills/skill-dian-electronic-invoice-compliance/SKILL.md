---
name: skill-dian-electronic-invoice-compliance
description: "Generate electronic invoices and credit notes in XML UBL 2.1 format, signed with a DIAN digital certificate, obtaining a valid CUFE from an authorized technological provider, and ensuring a minimum retention period of 7 years."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# DIAN Electronic Invoice Compliance

## Objetivo
Generate electronic invoices and credit notes in XML UBL 2.1 format, signed with a DIAN digital certificate, obtaining a valid CUFE from an authorized technological provider, and ensuring a minimum retention period of 7 years.

## Trigger
on code generation or modification of billing services

## Inputs
- Transaction data
- Invoice generation requests
- DIAN technological provider API client

## Procedimiento
1. Implement logic to generate XML UBL 2.1 compliant invoices and credit notes.
2. Integrate with the authorized DIAN technological provider to obtain CUFE and digital signatures.
3. Ensure idempotency for invoice generation to prevent duplicates for the same transaction.
4. Persist signed XML files for a minimum of 7 years in Cloud Storage.
5. Handle credit note generation for invoice cancellations or corrections, referencing the original invoice.

## Output esperado
Code that generates compliant XML UBL 2.1 invoices/credit notes, integrates with DIAN provider, and ensures data retention.

## Source refs (project)
- Resolución DIAN 000042/2020 — Facturación electrónica
- HU-005
- HU-006
