---
name: skill-data-type-compliance
description: "Ensure all unique identifiers, timestamps, and financial values adhere to the project's defined data type standards (UUID, ISO 8601, BigDecimal/MoneyAmount)."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Data Type Compliance for Core Entities

## Objetivo
Ensure all unique identifiers, timestamps, and financial values adhere to the project's defined data type standards (UUID, ISO 8601, BigDecimal/MoneyAmount).

## Trigger
on code generation or modification of data models

## Inputs
- Java class definitions
- API contracts (JSON schemas)
- Database schema definitions

## Procedimiento
1. Review data model definitions (e.g., DTOs, domain entities) for identifier fields (e.g., disputeId, accountId, transactionId) and confirm they are represented as UUID strings.
2. Verify timestamp fields (e.g., openedAt, detectedAt, authorizedAt) are represented as ISO 8601 formatted strings.
3. Check monetary amounts and balances (e.g., amount, balanceAfter) are represented as strings with decimal precision externally and `BigDecimal` with scale 2 internally, ideally wrapped in `MoneyAmount` value objects.
4. Confirm enumerated string values (e.g., resolution, status) use specific string literals, implying enums or constants in Java code.

## Output esperado
Data types for identifiers, timestamps, and financial values are correctly implemented according to standards.

## Source refs (project)
- identifier-format-uuid
- timestamp-format-iso-8601
- financial-value-representation
- financial-data-type-representation
- enumerated-string-values
