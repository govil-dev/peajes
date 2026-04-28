---
name: skill-java-data-validation-constraints
description: "Enforce data validation rules and business constraints (e.g., string length limits, non-negativity for numeric fields) at the earliest possible point in the application lifecycle to maintain data integrity."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Implementing Data Validation and Business Constraints in Java

## Objetivo
Enforce data validation rules and business constraints (e.g., string length limits, non-negativity for numeric fields) at the earliest possible point in the application lifecycle to maintain data integrity.

## Trigger
during development

## Inputs
- API contracts
- Business rules
- Domain models

## Procedimiento
1. Define validation rules for all input data, including string length limits, numeric ranges, and format requirements.
2. Utilize Java Bean Validation (JSR 380) with annotations (e.g., `@Size`, `@Min`, `@NotNull`, `@Pattern`) on DTOs and domain models.
3. Perform validation at API entry points (e.g., Spring `@Valid` or `@Validated` on controller method arguments).
4. Implement custom validation logic for complex business rules (e.g., `InsufficientBalanceException` for negative balances) within domain objects or services.
5. Ensure clear and specific error messages are returned to clients upon validation failure.
6. Write unit and integration tests to cover all defined validation rules and constraint violations.

## Output esperado
Application components that robustly validate all incoming data and enforce business constraints, preventing invalid data from corrupting the system state.

## Source refs (project)
- 5388fa7c-fae1-45ab-ab45-820fd2932e9b
- 88f3213d-e945-4cf7-bf2e-c59a9906f9b7
- HU-002
- HU-003
