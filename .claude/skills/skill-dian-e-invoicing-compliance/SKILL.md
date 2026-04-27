---
name: skill-dian-e-invoicing-compliance
description: "Develop electronic invoicing functionality fully compliant with DIAN Resolution 000042/2020, including generating XML UBL 2.1 invoices, digital signatures, CUFE, integration with authorized technological providers, and proper retention policies."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# DIAN Electronic Invoicing Compliance

## Objetivo
Develop electronic invoicing functionality fully compliant with DIAN Resolution 000042/2020, including generating XML UBL 2.1 invoices, digital signatures, CUFE, integration with authorized technological providers, and proper retention policies.

## Trigger
during development

## Inputs
- DIAN Resolution 000042/2020 specification
- billing domain events
- integration details for technological provider

## Procedimiento
1. Implement XML UBL 2.1 generation for electronic invoices and credit notes.
2. Integrate with a digital certificate for signing invoices as per DIAN requirements.
3. Generate and embed the CUFE (Código Único de Facturación Electrónica).
4. Develop an adapter for sending invoices to an authorized technological provider.
5. Implement a storage mechanism for signed XMLs with a minimum 7-year retention policy.

## Output esperado
Electronic invoicing system that generates, signs, and transmits compliant invoices and credit notes.

## Source refs (project)
- Resolución DIAN 000042/2020 — Facturación electrónica: Factura electrónica en formato XML UBL 2.1 firmada con certificado digital DIAN.
- Resolución DIAN 000042/2020
