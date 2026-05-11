---
name: skill-value-object-implementation
description: "Ensure immutable data types are implemented as Value Objects, with validation performed in their constructors to guarantee consistency."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [light]
---

# Value Object Implementation for Immutability

## Objetivo
Ensure immutable data types are implemented as Value Objects, with validation performed in their constructors to guarantee consistency.

## Trigger
on code generation or modification of domain models

## Inputs
- Java domain model classes

## Procedimiento
1. Identify domain concepts that represent values rather than entities (e.g., `MoneyAmount`, `GovernanceScore`, `CommitSha`).
2. Verify that these concepts are implemented as immutable classes (e.g., all fields are final, no setters).
3. Confirm that validation logic for the Value Object's constraints is performed within its constructor or a factory method, ensuring valid state upon creation.
4. Check for proper `equals()` and `hashCode()` implementations based on value equality.

## Output esperado
Value Objects are correctly implemented, ensuring immutability and valid state.

## Source refs (project)
- value-object
- 152eb76e-66e2-4e56-9a26-27b7290af8f6
- HU-003
