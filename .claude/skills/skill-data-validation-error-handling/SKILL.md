---
name: skill-data-validation-error-handling
description: "Implement comprehensive data validation for all input fields, enforcing length limits, non-negativity for numeric values, and using specific exceptions (e.g., `InsufficientBalanceException`) for business rule violations."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Robust Data Validation and Error Handling

## Objetivo
Implement comprehensive data validation for all input fields, enforcing length limits, non-negativity for numeric values, and using specific exceptions (e.g., `InsufficientBalanceException`) for business rule violations.

## Trigger
during development

## Inputs
- data models
- business rules
- API contracts

## Procedimiento
1. Define validation rules for all data fields based on business requirements.
2. Implement validation logic at the API entry points and domain model constructors.
3. Map validation failures to appropriate HTTP status codes and error messages.
4. Create custom exception types for specific business rule violations.
5. Ensure consistent error response formats across all services.

## Output esperado
Services that reject invalid data with clear error messages and handle business rule violations gracefully.

## Source refs (project)
- 5388fa7c-fae1-45ab-ab45-820fd2932e9b
- 88f3213d-e945-4cf7-bf2e-c59a9906f9b7
