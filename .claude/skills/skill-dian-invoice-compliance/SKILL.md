---
name: skill-dian-invoice-compliance
description: "Ensure electronic invoice generation (XML UBL 2.1, digital signature, CUFE) and credit note processes comply with Colombian DIAN Resolution 000042/2020, including data retention."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# DIAN Electronic Invoicing Compliance

## Objetivo
Ensure electronic invoice generation (XML UBL 2.1, digital signature, CUFE) and credit note processes comply with Colombian DIAN Resolution 000042/2020, including data retention.

## Trigger
on-code-change

## Inputs
- Java source code (billing context)
- XML generation logic
- Integration with DIAN technological provider

## Procedimiento
1. Verify that generated invoices are in XML UBL 2.1 format and digitally signed with a DIAN certificate.
2. Confirm the process for obtaining and embedding the CUFE (Código Único de Facturación Electrónica) from the authorized technological provider.
3. Check that credit notes are generated correctly for cancellations or corrections, referencing the original invoice.
4. Ensure that signed XML files are retained for a minimum of 7 years.
5. Validate idempotency for invoice and credit note generation to prevent duplicates.

## Output esperado
Electronic invoicing processes are fully compliant with DIAN regulations, ensuring legal and fiscal integrity.

## Source refs (project)
- Resolución DIAN 000042/2020 — Facturación electrónica
- HU-005
- HU-006
