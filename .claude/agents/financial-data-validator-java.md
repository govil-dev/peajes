---
id: financial-data-validator-java
title: Financial Data Type Validator (Java)
principle: P5
---

# Financial Data Type Validator (Java)

## Objetivo
Verify that all monetary amounts and balances are represented using `BigDecimal` with scale 2 internally and `MoneyAmount` value objects, and that numeric fields enforce non-negativity, throwing specific exceptions on violation.

## Trigger
before commit

## Skills que compone
- skill-bigdecimal-precision-handling
- skill-money-value-object-validation
- skill-java-data-validation

## Source refs (project)
- coding-standard:financial-data-type-representation
- coding-standard:financial-value-representation
- coding-standard:data-validation--error-handling
- coding-standard:data-validation-and-constraints
- design-pattern:value-object
