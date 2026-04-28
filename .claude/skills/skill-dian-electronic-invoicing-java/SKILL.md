---
name: skill-dian-electronic-invoicing-java
description: "Generate electronic invoices and credit notes compliant with Colombian DIAN Resolution 000042/2020, including XML UBL 2.1 formatting, digital signing, CUFE generation, and long-term retention."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Implementing DIAN Electronic Invoicing in Java

## Objetivo
Generate electronic invoices and credit notes compliant with Colombian DIAN Resolution 000042/2020, including XML UBL 2.1 formatting, digital signing, CUFE generation, and long-term retention.

## Trigger
during development

## Inputs
- DIAN 000042/2020 specification
- Technological provider API documentation
- Transaction data

## Procedimiento
1. Develop Java services to construct XML UBL 2.1 documents for invoices and credit notes.
2. Integrate with a DIAN-authorized technological provider for digital signing and CUFE (Código Único de Facturación Electrónica) generation.
3. Ensure all required fields (e.g., `transactionId`, `amount`, `taxAmount` with IVA 0 for tolls) are correctly mapped and populated.
4. Implement logic for invoice cancellation and credit note generation, ensuring proper referencing to original invoices.
5. Persist signed XML documents and generated PDFs in Cloud Storage for a minimum of 7 years.
6. Implement idempotency for invoice generation to prevent duplicate invoices for the same transaction.
7. Automate the process of sending invoices to the technological provider and handling their responses (success/failure/retries).
8. Write comprehensive integration tests covering XML generation, CUFE retrieval, and document storage.

## Output esperado
A robust invoicing system that automatically generates and manages DIAN-compliant electronic invoices and credit notes.

## Source refs (project)
- Resolución DIAN 000042/2020 — Facturación electrónica
- HU-005
- HU-006
