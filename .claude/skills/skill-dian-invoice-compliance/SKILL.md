---
name: skill-dian-invoice-compliance
description: "Produce electronic invoices and credit notes that strictly comply with Colombian DIAN Resolution 000042/2020, including XML UBL 2.1 format, digital signature, CUFE generation, and mandatory retention policies."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Generate DIAN Compliant Electronic Invoices

## Objetivo
Produce electronic invoices and credit notes that strictly comply with Colombian DIAN Resolution 000042/2020, including XML UBL 2.1 format, digital signature, CUFE generation, and mandatory retention policies.

## Trigger
on code change in billing service or invoice generation logic

## Inputs
- Billing service code
- Invoice generation logic
- Credit note generation logic

## Procedimiento
1. Utilize a certified library or integrate with an authorized technology provider for XML UBL 2.1 invoice generation.
2. Ensure invoices are digitally signed with a DIAN-approved certificate.
3. Implement the process for obtaining the CUFE (Código Único de Facturación Electrónica) for each invoice and credit note.
4. Verify that generated XML files are retained for a minimum of 7 years.
5. Ensure credit notes correctly reference the original invoice and follow the same compliance rules.

## Output esperado
Electronic invoices and credit notes are generated correctly, are DIAN compliant, and are stored for the required retention period.

## Source refs (project)
- Resolución DIAN 000042/2020 — Facturación electrónica
- HU-005:business_rules:1
- HU-005:business_rules:3
- HU-005:business_rules:5
- HU-006:business_rules:1
- HU-006:business_rules:6
