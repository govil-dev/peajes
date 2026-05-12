---
name: skill-java-value-object-pattern
description: "Apply the Value Object pattern for immutable data types, ensuring validation within constructors and immutability for enhanced domain integrity."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Java Value Object Pattern Implementation

## Objetivo
Apply the Value Object pattern for immutable data types, ensuring validation within constructors and immutability for enhanced domain integrity.

## Trigger
on-code-change

## Inputs
- Java source code (domain models)

## Procedimiento
1. Identify domain concepts that should be modeled as Value Objects (e.g., MoneyAmount, GovernanceScore).
2. Verify that Value Objects are immutable (all fields are final, no setters).
3. Ensure validation logic is performed within the constructor or factory methods of Value Objects.
4. Check for correct implementation of `equals()` and `hashCode()` based on value equality.
5. Confirm that Value Objects are used consistently across the domain layer.

## Output esperado
Domain models are robust, immutable, and self-validating through the consistent use of Value Objects.

## Source refs (project)
- value-object
- 152eb76e-66e2-4e56-9a26-27b7290af8f6
- HU-003
