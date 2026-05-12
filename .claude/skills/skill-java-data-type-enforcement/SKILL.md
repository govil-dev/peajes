---
name: skill-java-data-type-enforcement
description: "Ensure all data types adhere to project-defined formats (UUID, ISO 8601, BigDecimal for financial values, enums for fixed options) within Java code."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Java Data Type Enforcement

## Objetivo
Ensure all data types adhere to project-defined formats (UUID, ISO 8601, BigDecimal for financial values, enums for fixed options) within Java code.

## Trigger
on-code-change

## Inputs
- Java source code
- API contracts
- Database schemas

## Procedimiento
1. Verify that all unique identifiers are represented as UUID strings.
2. Confirm that all timestamp fields use ISO 8601 formatted strings.
3. Check that monetary amounts and balances are represented as BigDecimal internally and MoneyAmount value objects, preserving currency.
4. Ensure fields with predefined options utilize Java enums or constants for specific string literals.

## Output esperado
Code adheres to specified data type formats, with appropriate types and validations.

## Source refs (project)
- identifier-format-uuid
- timestamp-format-iso-8601
- financial-value-representation
- enumerated-string-values
- financial-data-type-representation
