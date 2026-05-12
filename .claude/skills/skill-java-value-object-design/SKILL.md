---
name: skill-java-value-object-design
description: "Design and implement immutable value objects for core domain concepts, ensuring validation upon construction, proper equality semantics, and clear representation of business values."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Design and Implement Java Value Objects

## Objetivo
Design and implement immutable value objects for core domain concepts, ensuring validation upon construction, proper equality semantics, and clear representation of business values.

## Trigger
on creation or modification of domain model classes

## Inputs
- Domain model definitions
- Existing data classes

## Procedimiento
1. Identify domain concepts that represent values rather than entities (e.g., `MoneyAmount`, `PassId`, `LicensePlate`).
2. Implement value objects as immutable classes with `final` fields and private constructors.
3. Perform all necessary validation within the constructor or factory methods to ensure the object is always in a valid state.
4. Override `equals()` and `hashCode()` methods based on the value of their fields, not object identity.
5. Avoid setters and ensure any modification results in a new instance.

## Output esperado
Domain value objects are immutable, self-validating, and correctly implement value-based equality.

## Source refs (project)
- 152eb76e-66e2-4e56-9a26-27b7290af8f6
- HU-003:business_rules:9
