---
name: skill-enforce-data-validation-rules
description: "Apply all specified data validation rules for string lengths, numeric ranges, and business-specific constraints, throwing appropriate exceptions on violation."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Enforce Data Validation and Business Constraints

## Objetivo
Apply all specified data validation rules for string lengths, numeric ranges, and business-specific constraints, throwing appropriate exceptions on violation.

## Trigger
on data model or API contract definition

## Inputs
- Data Transfer Objects (DTOs)
- Domain models
- Business rule specifications

## Procedimiento
1. Identify all data fields requiring validation based on coding standards and business rules.
2. Implement length limits for string fields (e.g., using `@Size` annotations or manual checks).
3. Enforce non-negativity and other numeric constraints for financial or quantity fields.
4. Define and throw specific business exceptions (e.g., `InsufficientBalanceException`) for rule violations.
5. Integrate validation logic into DTOs, domain models, or service layers as appropriate.

## Output esperado
Data fields validated against specified constraints, with clear error handling and custom exceptions for business rule violations.

## Source refs (project)
- 5388fa7c-fae1-45ab-ab45-820fd2932e9b
- 88f3213d-e945-4cf7-bf2e-c59a9906f9b7
- data-validation--error-handling
- data-validation-and-constraints
