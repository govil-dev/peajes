---
name: skill-design-implement-value-objects
description: "Create immutable value objects for specific data types (e.g., `MoneyAmount`, `UUID`, `ISO8601Timestamp`) with validation performed in their constructors to ensure data integrity."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [standard]
---

# Design and Implement Immutable Value Objects

## Objetivo
Create immutable value objects for specific data types (e.g., `MoneyAmount`, `UUID`, `ISO8601Timestamp`) with validation performed in their constructors to ensure data integrity.

## Trigger
on defining new domain concepts or data types

## Inputs
- Domain model definitions
- Data validation rules
- Coding standards for data types

## Procedimiento
1. Identify domain concepts that represent values rather than entities (e.g., amounts, identifiers, dates, enumerations).
2. Define classes for these concepts, making them immutable (all fields `final`, no setters).
3. Implement constructors that perform all necessary validation (e.g., range checks, format checks, null checks).
4. Override `equals()` and `hashCode()` based on the value, not identity.
5. Use these value objects consistently throughout the domain layer.

## Output esperado
Robust, self-validating, and immutable value objects that enhance domain clarity and prevent invalid states.

## Source refs (project)
- 152eb76e-66e2-4e56-9a26-27b7290af8f6
- value-object
- financial-data-type-representation
- identifier-format-uuid
- timestamp-format-iso-8601
