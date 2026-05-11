---
name: skill-dian-invoice-xml-compliance
description: "Validate that electronic invoices are generated in XML UBL 2.1 format, digitally signed with a DIAN certificate, include a valid CUFE, and adhere to the specified retention policies (minimum 7 years)."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# DIAN Electronic Invoice XML Compliance

## Objetivo
Validate that electronic invoices are generated in XML UBL 2.1 format, digitally signed with a DIAN certificate, include a valid CUFE, and adhere to the specified retention policies (minimum 7 years).

## Trigger
on code review

## Inputs
- Invoice generation service code
- XML schema definitions
- Data retention policies and storage configurations
- Integration code with DIAN technological provider

## Procedimiento
1. Review the invoice generation logic to ensure output is XML UBL 2.1 compliant.
2. Verify the integration with the DIAN authorized technological provider for CUFE generation and digital signing.
3. Confirm that credit notes are generated for cancellations/corrections, referencing the original invoice and having their own CUFE.
4. Check the data retention policy implementation for signed XML files, ensuring they are stored for at least 7 years.
5. Validate that the `iva` field for toll lines is consistently set to 0.

## Output esperado
Electronically generated invoices that fully comply with DIAN Resolution 000042/2020, including format, signing, CUFE, and retention.

## Source refs (project)
- Resolución DIAN 000042/2020 — Facturación electrónica
- HU-005
- HU-006
