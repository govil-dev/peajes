---
name: skill-financial-data-type-management
description: "Represent all monetary amounts and balances using `BigDecimal` with a scale of 2, encapsulated within `MoneyAmount` value objects that preserve currency, to ensure precision and prevent rounding errors."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Financial Data Type Representation

## Objetivo
Represent all monetary amounts and balances using `BigDecimal` with a scale of 2, encapsulated within `MoneyAmount` value objects that preserve currency, to ensure precision and prevent rounding errors.

## Trigger
during development

## Inputs
- financial data fields
- business requirements for monetary precision

## Procedimiento
1. Define a `MoneyAmount` value object with `BigDecimal` for amount and a `Currency` field.
2. Ensure `MoneyAmount` is immutable and performs validation in its constructor.
3. Use `BigDecimal` for all arithmetic operations involving money.
4. Convert external string representations of money to `MoneyAmount` upon input and vice-versa for output.
5. Implement serialization/deserialization for `MoneyAmount` consistently.

## Output esperado
Consistent and accurate handling of all financial data throughout the system.

## Source refs (project)
- 58c84ffe-71f3-456c-927d-1d2647c2b26b
- 280e17f5-f799-4390-abec-9f5156240673
- 152eb76e-66e2-4e56-9a26-27b7290af8f6
