---
name: skill-generate-dian-compliant-invoices
description: "Produce electronic invoices in XML UBL 2.1 format, digitally signed with a DIAN certificate, including CUFE, and manage their submission to an authorized technological provider and retention for 7 years."
metadata:
  framework_principle: P2
  enforcement_mode: verify
  criticality_level: [standard]
---

# Generate DIAN Compliant Electronic Invoices

## Objetivo
Produce electronic invoices in XML UBL 2.1 format, digitally signed with a DIAN certificate, including CUFE, and manage their submission to an authorized technological provider and retention for 7 years.

## Trigger
on invoice generation or correction

## Inputs
- Billing data
- DIAN regulations (Resolución 000042/2020)
- Digital certificate management

## Procedimiento
1. Implement a module responsible for generating XML UBL 2.1 compliant electronic invoices.
2. Integrate with a digital signature service using the DIAN certificate.
3. Ensure the invoice includes the Código Único de Facturación Electrónica (CUFE).
4. Develop an adapter to send the signed XML invoice to the authorized technological provider.
5. Implement a storage mechanism for signed XMLs, ensuring retention for a minimum of 7 years.
6. Provide functionality for generating electronic credit notes for cancellations or corrections.

## Output esperado
Electronically signed XML UBL 2.1 invoices, successfully submitted to the DIAN authorized provider, and securely retained, with support for credit notes.

## Source refs (project)
- Resolución DIAN 000042/2020 — Facturación electrónica: Factura electrónica en formato XML UBL 2.1 firmada con certificado digital DIAN.
- Resolución DIAN 000042/2020 — Facturación electrónica: Envío al proveedor tecnológico autorizado (habilitado por DIAN) antes de entregar al adquirente.
- Resolución DIAN 000042/2020 — Facturación electrónica: Nota crédito electrónica para anulaciones o correcciones.
- Resolución DIAN 000042/2020 — Facturación electrónica: Numeración autorizada con CUFE (Código Único de Facturación Electrónica).
- Resolución DIAN 000042/2020 — Facturación electrónica: Retención de XML firmados por 5 años mínimo (el sistema los retiene 7 años por política interna).
