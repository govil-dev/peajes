---
name: skill-financial-data-type-representation
description: "Enforce the consistent use of `java.math.BigDecimal` with a scale of 2 for all monetary amounts and balances, encapsulated within `MoneyAmount` value objects that preserve currency information."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Financial Data Type Representation

## Objetivo
Enforce the consistent use of `java.math.BigDecimal` with a scale of 2 for all monetary amounts and balances, encapsulated within `MoneyAmount` value objects that preserve currency information.

## Trigger
on data model or DTO creation/modification involving financial data

## Inputs
- Data model definitions
- DTOs for financial transactions
- Business logic involving monetary calculations

## Procedimiento
1. Review all data models and DTOs that handle financial amounts (e.g., `amount`, `balanceAfter`, `refundAmount`).
2. Replace `double`, `float`, or `long` types with `BigDecimal` for internal representation.
3. Ensure `BigDecimal` instances are initialized with a scale of 2 and appropriate rounding modes.
4. Encapsulate `BigDecimal` and `Currency` within immutable `MoneyAmount` value objects.
5. Validate `MoneyAmount` value objects in their constructors to ensure non-negativity for balances and other business rules.

## Output esperado
Code using `BigDecimal` and `MoneyAmount` value objects for all financial data, with constructor validation.

## Source refs (project)
- financial-data-type-representation
- financial-value-representation
- value-object
- HU-003
- HU-004
