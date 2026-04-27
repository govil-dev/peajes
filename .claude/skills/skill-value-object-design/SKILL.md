---
name: skill-value-object-design
description: "Design and implement immutable value objects for core domain concepts, ensuring validation within constructors and strict adherence to specific data formats (e.g., UUIDs for identifiers, ISO 8601 for timestamps, enums for predefined string values)."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Value Object Design and Implementation

## Objetivo
Design and implement immutable value objects for core domain concepts, ensuring validation within constructors and strict adherence to specific data formats (e.g., UUIDs for identifiers, ISO 8601 for timestamps, enums for predefined string values).

## Trigger
during development

## Inputs
- domain model definitions
- coding standards for data types

## Procedimiento
1. Identify suitable candidates for value objects (e.g., IDs, amounts, dates, enumerated strings).
2. Define value objects as immutable classes with private constructors and factory methods.
3. Implement validation logic within constructors or `__post_init__` (if applicable) to ensure valid state upon creation.
4. Override `equals()` and `hashCode()` based on value equality.
5. Ensure consistent use of UUIDs, ISO 8601 strings, and enums/constants for their respective data types.

## Output esperado
Robust, self-validating value objects that enhance domain clarity and reduce errors.

## Source refs (project)
- 152eb76e-66e2-4e56-9a26-27b7290af8f6
- 58c84ffe-71f3-456c-927d-1d2647c2b26b
- 6bcca597-0794-4870-ad22-fa067b8bca72
- 6df3d29a-591e-4cda-96f5-548d4507bc88
- 6d1329f3-af63-4331-b3cf-4c83abb1496f
