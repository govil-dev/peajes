---
name: skill-apply-standard-id-timestamp-formats
description: "Ensure all unique identifiers are represented as UUID strings and all timestamp fields are formatted as ISO 8601 strings."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [light]
---

# Apply Standard Identifier and Timestamp Formats

## Objetivo
Ensure all unique identifiers are represented as UUID strings and all timestamp fields are formatted as ISO 8601 strings.

## Trigger
on defining new data fields

## Inputs
- Data model definitions
- API contracts
- Event schemas

## Procedimiento
1. For any new unique identifier (e.g., `disputeId`, `accountId`), use `java.util.UUID` and represent it as a string.
2. For all timestamp fields (e.g., `openedAt`, `detectedAt`), use `java.time.Instant` or `OffsetDateTime` and serialize/deserialize them to/from ISO 8601 formatted strings.
3. Avoid using epoch milliseconds or other non-standard date/time formats in external interfaces.

## Output esperado
Consistent and universally parsable identifiers and timestamps across all system interfaces and data stores.

## Source refs (project)
- 6d1329f3-af63-4331-b3cf-4c83abb1496f
- 6df3d29a-591e-4cda-96f5-548d4507bc88
- identifier-format-uuid
- timestamp-format-iso-8601
