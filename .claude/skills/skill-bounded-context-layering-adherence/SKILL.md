---
name: skill-bounded-context-layering-adherence
description: "Ensure all code within each bounded context strictly adheres to the `domain/`, `application/`, and `infrastructure/` layering, promoting clear separation of concerns and maintainability."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [light]
---

# Bounded Context Layering Adherence

## Objetivo
Ensure all code within each bounded context strictly adheres to the `domain/`, `application/`, and `infrastructure/` layering, promoting clear separation of concerns and maintainability.

## Trigger
on new module creation or significant refactoring

## Inputs
- Project package structure
- New or modified source code files

## Procedimiento
1. Review package structure for new or modified code within a bounded context.
2. Verify that domain logic resides in `domain/` (model, services, repositories interfaces).
3. Confirm application logic (use cases, orchestrators) is in `application/`.
4. Ensure infrastructure concerns (web controllers, database implementations, external clients) are in `infrastructure/`.
5. Prevent cross-layer dependencies that violate the architectural principles (e.g., `domain` depending on `infrastructure`).

## Output esperado
Code organized according to the `domain/application/infrastructure` layering within each bounded context.

## Source refs (project)
- structure_definition:9eef6827-df7c-4dae-b331-b0ff0bf37f38
