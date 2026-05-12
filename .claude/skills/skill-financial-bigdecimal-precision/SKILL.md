---
name: skill-financial-bigdecimal-precision
description: "Verify that all monetary amounts and balances are represented and processed using `BigDecimal` with a defined scale (e.g., 2) and encapsulated within `MoneyAmount` value objects to preserve currency and prevent precision errors."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Financial Data Type Precision Enforcement

## Objetivo
Verify that all monetary amounts and balances are represented and processed using `BigDecimal` with a defined scale (e.g., 2) and encapsulated within `MoneyAmount` value objects to preserve currency and prevent precision errors.

## Trigger
on code review

## Inputs
- Domain model classes
- Service layer code handling financial calculations
- API DTOs for financial data

## Procedimiento
1. Scan data models, DTOs, and service methods for fields representing monetary values or balances.
2. Confirm that `BigDecimal` is used for internal representation, with a consistent scale (e.g., `setScale(2, RoundingMode.HALF_EVEN)`).
3. Ensure `MoneyAmount` value objects are utilized to encapsulate `BigDecimal` and currency information, with validation in their constructors.
4. Check for correct conversion to and from string representations with decimal precision for external interfaces.
5. Validate that arithmetic operations on financial data use `BigDecimal` methods and handle rounding appropriately.

## Output esperado
Consistent and accurate handling of financial data types throughout the application, preventing rounding errors and ensuring currency preservation.

## Source refs (project)
- financial-data-type-representation
- financial-value-representation
- value-object
- HU-003
- HU-004
