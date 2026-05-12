---
name: skill-financial-data-precision
description: "Correctly represent and process all monetary amounts and balances using `BigDecimal` with appropriate scale and `MoneyAmount` value objects to ensure precision and currency preservation, avoiding floating-point inaccuracies."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Ensure Financial Data Precision and Representation

## Objetivo
Correctly represent and process all monetary amounts and balances using `BigDecimal` with appropriate scale and `MoneyAmount` value objects to ensure precision and currency preservation, avoiding floating-point inaccuracies.

## Trigger
on code change in domain or application layers involving financial data

## Inputs
- Domain model classes
- Service logic involving financial calculations

## Procedimiento
1. Identify all fields representing monetary amounts or balances.
2. Ensure these fields are typed as `java.math.BigDecimal` with a defined scale (e.g., 2 for COP).
3. Encapsulate `BigDecimal` and `Currency` within an immutable `MoneyAmount` value object.
4. Verify that all arithmetic operations on monetary values use `BigDecimal` methods and handle rounding modes explicitly.

## Output esperado
All financial data is represented by `BigDecimal` within `MoneyAmount` value objects, ensuring accurate calculations and currency context.

## Source refs (project)
- 58c84ffe-71f3-456c-927d-1d2647c2b26b
- 280e17f5-f799-4390-abec-9f5156240673
- 152eb76e-66e2-4e56-9a26-27b7290af8f6
- HU-003:business_rules:9
