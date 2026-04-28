---
name: skill-data-format-uuid-iso8601
description: "Ensure all unique identifiers are represented as UUID strings and all timestamp fields are formatted as ISO 8601 strings for consistency and interoperability."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Enforcing UUID and ISO 8601 Data Formats

## Objetivo
Ensure all unique identifiers are represented as UUID strings and all timestamp fields are formatted as ISO 8601 strings for consistency and interoperability.

## Trigger
during development

## Inputs
- API contracts
- Database schemas
- Domain models

## Procedimiento
1. Identify all fields designated as unique identifiers (e.g., `disputeId`, `accountId`, `transactionId`).
2. Ensure these fields are typed as `java.util.UUID` in Java models and serialized as UUID strings.
3. Identify all timestamp fields (e.g., `openedAt`, `detectedAt`, `authorizedAt`).
4. Ensure these fields are typed as `java.time.Instant` or `java.time.OffsetDateTime` in Java models and serialized as ISO 8601 strings (e.g., `yyyy-MM-dd'T'HH:mm:ss'Z'`).
5. Use appropriate Jackson annotations (`@JsonFormat`) or custom serializers/deserializers for consistent JSON serialization/deserialization.
6. Implement validation to reject invalid UUID or ISO 8601 formats on input.

## Output esperado
Data models and API endpoints that consistently use UUIDs for identifiers and ISO 8601 for timestamps.

## Source refs (project)
- 6d1329f3-af63-4331-b3cf-4c83abb1496f
- 6df3d29a-591e-4cda-96f5-548d4507bc88
- HU-002
- HU-005
