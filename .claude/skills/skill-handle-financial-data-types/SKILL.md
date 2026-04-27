---
name: skill-handle-financial-data-types
description: "Represent monetary amounts and balances using `BigDecimal` internally with a defined scale, and encapsulate them within `MoneyAmount` value objects that preserve currency information."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Handle Financial Data Types with Precision and Currency

## Objetivo
Represent monetary amounts and balances using `BigDecimal` internally with a defined scale, and encapsulate them within `MoneyAmount` value objects that preserve currency information.

## Trigger
on financial data definition or manipulation

## Inputs
- Monetary amount fields
- Balance fields
- Currency codes

## Procedimiento
1. Define a `MoneyAmount` value object that encapsulates `BigDecimal` for the amount and a `Currency` type.
2. Ensure `BigDecimal` is initialized with a specific scale (e.g., 2) and rounding mode for all calculations.
3. Use `MoneyAmount` for all monetary fields in domain models, DTOs, and service interfaces.
4. Implement serialization/deserialization logic to represent `MoneyAmount` as strings with decimal precision in external interfaces.
5. Perform validation within the `MoneyAmount` constructor to ensure non-negative values where applicable.

## Output esperado
Consistent and precise handling of financial data throughout the system, preventing floating-point inaccuracies and ensuring currency context.

## Source refs (project)
- 58c84ffe-71f3-456c-927d-1d2647c2b26b
- 280e17f5-f799-4390-abec-9f5156240673
- financial-data-type-representation
- financial-value-representation
- value-object
- 152eb76e-66e2-4e56-9a26-27b7290af8f6
