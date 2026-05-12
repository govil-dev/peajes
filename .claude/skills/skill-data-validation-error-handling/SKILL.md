---
name: skill-data-validation-error-handling
description: "Ensure all input data fields adhere to specified constraints (e.g., length limits, non-negativity) and that business rule violations trigger specific, well-defined exceptions."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Data Validation and Business Rule Error Handling

## Objetivo
Ensure all input data fields adhere to specified constraints (e.g., length limits, non-negativity) and that business rule violations trigger specific, well-defined exceptions.

## Trigger
on code generation or modification of data models or business logic

## Inputs
- Java DTOs and domain entities
- Service layer business logic
- Spring WebFlux exception handlers

## Procedimiento
1. Review DTOs and domain models for field-level validation annotations or explicit validation logic (e.g., `@Size`, `@Min`, custom validators).
2. Verify that numeric fields representing balances or amounts enforce non-negativity where required.
3. Check that business rule violations (e.g., insufficient balance, dispute window expired) throw specific, custom exceptions (e.g., `InsufficientBalanceException`, `DisputeWindowExpiredException`).
4. Ensure these exceptions are handled gracefully in the application layer, translating to appropriate HTTP status codes and informative error messages in API responses.

## Output esperado
Data inputs are rigorously validated, and business rule violations result in predictable, specific error handling.

## Source refs (project)
- data-validation-and-constraints
- data-validation--error-handling
- 88f3213d-e945-4cf7-bf2e-c59a9906f9b7
- 5388fa7c-fae1-45ab-ab45-820fd2932e9b
- HU-001
- HU-009
