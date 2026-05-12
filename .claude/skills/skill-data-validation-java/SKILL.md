---
name: skill-data-validation-java
description: "Apply strict validation rules for all data fields, including string length limits, numeric non-negativity, UUID format for identifiers, ISO 8601 for timestamps, and enumerated values for predefined options."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Enforce Data Validation and Constraints in Java

## Objetivo
Apply strict validation rules for all data fields, including string length limits, numeric non-negativity, UUID format for identifiers, ISO 8601 for timestamps, and enumerated values for predefined options.

## Trigger
on code change in DTOs, domain models, or service input validation

## Inputs
- DTOs and domain model classes
- Service input validation logic

## Procedimiento
1. Use Java Bean Validation (JSR 380) annotations (`@Size`, `@Min`, `@Pattern`, `@NotNull`) on DTOs and domain models.
2. Implement custom validators for complex business rules (e.g., `InsufficientBalanceException`).
3. Ensure all unique identifiers are validated as UUID strings.
4. Verify all timestamp fields adhere to ISO 8601 format.
5. Use Java enums or constants for fields with predefined sets of options.

## Output esperado
All data inputs and domain objects are validated against specified constraints, preventing invalid data from entering the system.

## Source refs (project)
- 5388fa7c-fae1-45ab-ab45-820fd2932e9b
- 88f3213d-e945-4cf7-bf2e-c59a9906f9b7
- 6d1329f3-af63-4331-b3cf-4c83abb1496f
- 6df3d29a-591e-4cda-96f5-548d4507bc88
- 6bcca597-0794-4870-ad22-fa067b8bca72
- HU-001:business_rules:3
- HU-003:business_rules:8
- HU-009:business_rules:1
