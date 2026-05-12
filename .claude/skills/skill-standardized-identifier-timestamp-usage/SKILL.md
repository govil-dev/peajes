---
name: skill-standardized-identifier-timestamp-usage
description: "Enforce the consistent use of UUID strings for all unique identifiers and ISO 8601 formatted strings for all timestamp fields across the system."
metadata:
  framework_principle: P5
  enforcement_mode: warn
  criticality_level: [light]
---

# Standardized Identifier and Timestamp Usage

## Objetivo
Enforce the consistent use of UUID strings for all unique identifiers and ISO 8601 formatted strings for all timestamp fields across the system.

## Trigger
on data model or API contract definition

## Inputs
- Data model definitions
- API request/response schemas
- Event schemas

## Procedimiento
1. Identify all fields intended as unique identifiers (e.g., `disputeId`, `accountId`, `transactionId`).
2. Ensure these fields are represented as UUID strings.
3. Identify all timestamp fields (e.g., `openedAt`, `detectedAt`, `authorizedAt`).
4. Ensure these fields are represented as ISO 8601 formatted strings.
5. Implement validation to check for correct UUID and ISO 8601 formats upon data ingress.

## Output esperado
Consistent use of UUIDs and ISO 8601 timestamps throughout the codebase and data contracts.

## Source refs (project)
- identifier-format-uuid
- timestamp-format-iso-8601
- HU-002
