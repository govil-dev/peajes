---
name: skill-uuid-iso8601-formats
description: "Enforce the consistent use of UUID strings for all unique identifiers (e.g., `disputeId`, `accountId`, `transactionId`) and ISO 8601 formatted strings for all timestamp fields (e.g., `openedAt`, `detectedAt`)."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Standardized Identifier and Timestamp Formats

## Objetivo
Enforce the consistent use of UUID strings for all unique identifiers (e.g., `disputeId`, `accountId`, `transactionId`) and ISO 8601 formatted strings for all timestamp fields (e.g., `openedAt`, `detectedAt`).

## Trigger
on code review

## Inputs
- Domain model classes
- API DTOs
- Kafka event schemas
- Database column types

## Procedimiento
1. Scan data models, DTOs, and API contracts for identifier fields.
2. Verify that all unique identifiers are declared and generated as UUID strings.
3. Examine timestamp fields in data models, DTOs, and event payloads.
4. Confirm that all timestamps are represented as ISO 8601 formatted strings (e.g., `Instant.now().toString()`).
5. Check for proper parsing and serialization of these formats across service boundaries and persistence layers.

## Output esperado
Consistent and correct formatting of identifiers and timestamps across the entire system, improving interoperability and data integrity.

## Source refs (project)
- identifier-format-uuid
- timestamp-format-iso-8601
- HU-002
