---
id: peajes-data-integrity-reviewer
title: Peajes Data Integrity Reviewer
principle: P5
---

# Peajes Data Integrity Reviewer

## Objetivo
Ensure all data types, financial calculations, and DIAN invoicing comply with project standards and regulatory requirements, including idempotency for critical operations in Java Spring Boot applications.

## Trigger
src/main/java/**/*.java

## Skills que compone
- skill-peajes-java-data-type-validation
- skill-peajes-financial-logic-review
- skill-peajes-dian-invoicing-compliance
- skill-peajes-idempotency-pattern-validation

## Source refs (project)
- coding-standard:identifier-format-uuid
- coding-standard:timestamp-format-iso-8601
- coding-standard:financial-value-representation
- coding-standard:enumerated-string-values
- coding-standard:idempotent-event-processing
- coding-standard:data-validation-and-constraints
- coding-standard:kafka-event-delivery--idempotency
- design-pattern:value-object
- design-pattern:idempotent-consumer
- compliance-requirement:Resolución DIAN 000042/2020 — Facturación electrónica: Factura electrónica en formato XML UBL 2.1 firmada con certificado digital DIAN; envío a proveedor tecnológico autorizado antes de entregar al adquirente; nota crédito electrónica para anulaciones/correcciones; numeración autorizada con CUFE; retención de XML firmados por 5 años mínimo (sistema retiene 7 años).
- use-case:e841da87-af8a-4d88-8073-e8a57999b00f
- use-case:48c72c87-fde4-41bb-b2bb-12ba688f00ed
- use-case:e9bffde0-4a55-45b8-8fcb-a652c8e076fd
- use-case:b638153c-2516-43a0-a304-5e4e4c895790
