---
name: skill-java-financial-value-objects
description: "Accurately represent monetary amounts and balances using immutable Value Objects with `BigDecimal` and `Currency` to prevent precision errors and ensure business rule enforcement."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Representing Financial Data with Java Value Objects

## Objetivo
Accurately represent monetary amounts and balances using immutable Value Objects with `BigDecimal` and `Currency` to prevent precision errors and ensure business rule enforcement.

## Trigger
during development

## Inputs
- Domain model definitions
- Business rules for financial operations

## Procedimiento
1. Define Value Objects (e.g., `MoneyAmount`, `Balance`) for all financial data fields.
2. Use `java.math.BigDecimal` with a defined scale (e.g., 2 for COP) for internal representation of amounts.
3. Include `java.util.Currency` within Value Objects to explicitly track the currency.
4. Implement immutability for Value Objects, performing validation in their constructors (e.g., non-negativity for balances).
5. Provide methods for arithmetic operations (add, subtract) that return new instances of the Value Object.
6. Ensure external representations (e.g., JSON) use strings with decimal precision.
7. Write unit tests to cover precision, currency consistency, and business rule validation within Value Objects.

## Output esperado
Financial data types that are robust, immutable, and enforce business invariants, preventing common monetary calculation errors.

## Source refs (project)
- 58c84ffe-71f3-456c-927d-1d2647c2b26b
- 280e17f5-f799-4390-abec-9f5156240673
- 152eb76e-66e2-4e56-9a26-27b7290af8f6
- 5388fa7c-fae1-45ab-ab45-820fd2932e9b
- 88f3213d-e945-4cf7-bf2e-c59a9906f9b7
- HU-003
