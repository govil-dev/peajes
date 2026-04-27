---
name: skill-manage-enumerated-string-values
description: "Use Java enums or constants for fields with a predefined set of options (e.g., resolution, failureReason, status) to ensure consistency, type safety, and prevent arbitrary string values."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [light]
---

# Manage Enumerated String Values with Enums

## Objetivo
Use Java enums or constants for fields with a predefined set of options (e.g., resolution, failureReason, status) to ensure consistency, type safety, and prevent arbitrary string values.

## Trigger
on defining new data fields with fixed options

## Inputs
- Data model definitions
- API contracts
- Coding standards for enumerated values

## Procedimiento
1. Identify all fields that represent a fixed set of options.
2. Define a Java enum for each such field, mapping the predefined string literals to enum constants.
3. Use these enums in domain models, DTOs, and API contracts.
4. Implement custom serialization/deserialization if necessary to convert enum values to/from their string representations for external interfaces.
5. Ensure validation logic checks for valid enum values when receiving input.

## Output esperado
Type-safe and consistent representation of enumerated values, reducing errors and improving code readability.

## Source refs (project)
- 6bcca597-0794-4870-ad22-fa067b8bca72
- enumerated-string-values
