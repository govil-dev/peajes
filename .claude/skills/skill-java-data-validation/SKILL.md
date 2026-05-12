---
name: skill-java-data-validation
description: "Implement robust data validation for string lengths, numeric constraints (e.g., non-negativity), and custom exception handling for business rule violations."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Java Data Validation and Exception Handling

## Objetivo
Implement robust data validation for string lengths, numeric constraints (e.g., non-negativity), and custom exception handling for business rule violations.

## Trigger
on-code-change

## Inputs
- Java source code (DTOs, domain models, service methods)

## Procedimiento
1. Review input DTOs and domain models for appropriate validation annotations or explicit checks.
2. Verify that string fields adhere to specified length limits.
3. Ensure numeric fields (e.g., balances) enforce non-negativity and other business rules.
4. Check for the use of specific, meaningful exceptions (e.g., `InsufficientBalanceException`) when business rules are violated.
5. Confirm that validation logic is applied at the appropriate layer (e.g., domain model constructors, service layer).

## Output esperado
Data integrity is maintained through comprehensive validation, and business rule violations are handled gracefully with specific exceptions.

## Source refs (project)
- data-validation-and-constraints
- data-validation--error-handling
- 88f3213d-e945-4cf7-bf2e-c59a9906f9b7
- 5388fa7c-fae1-45ab-ab45-820fd2932e9b
- HU-003
