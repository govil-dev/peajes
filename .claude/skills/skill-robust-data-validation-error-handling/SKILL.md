---
name: skill-robust-data-validation-error-handling
description: "Implement comprehensive data validation for all input fields, including string length limits, numeric constraints (e.g., non-negativity), and use specific exceptions for business rule violations."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Robust Data Validation and Error Handling

## Objetivo
Implement comprehensive data validation for all input fields, including string length limits, numeric constraints (e.g., non-negativity), and use specific exceptions for business rule violations.

## Trigger
on API endpoint or business logic creation/modification

## Inputs
- API request DTOs
- Domain model constructors
- Business service methods

## Procedimiento
1. Review all API endpoints and internal data processing entry points.
2. Apply validation annotations (e.g., `@Size`, `@Min`, `@NotNull`) for DTOs and domain objects.
3. Implement custom validation logic for complex business rules (e.g., `InsufficientBalanceException`).
4. Ensure validation failures result in appropriate HTTP status codes (e.g., 400 Bad Request, 422 Unprocessable Entity) and clear error messages.
5. Centralize exception handling using Spring's `@ControllerAdvice` or similar mechanisms.

## Output esperado
Code with explicit data validation rules and custom exceptions for business rule violations.

## Source refs (project)
- data-validation--error-handling
- data-validation-and-constraints
- HU-009
