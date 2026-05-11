---
name: skill-bounded-context-layering
description: "Verify that the project's package structure strictly adheres to the defined layering within each bounded context (`domain/`, `application/`, `infrastructure/`) to maintain clarity, modularity, and separation of concerns."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Bounded Context Layering Adherence

## Objetivo
Verify that the project's package structure strictly adheres to the defined layering within each bounded context (`domain/`, `application/`, `infrastructure/`) to maintain clarity, modularity, and separation of concerns.

## Trigger
on code review

## Inputs
- Project source code directory structure
- Class definitions and their package declarations

## Procedimiento
1. Inspect the project's package structure, starting from the root `co.quind.peajes` and its bounded context sub-packages (e.g., `co.quind.peajes.tollcollection`).
2. Confirm that each bounded context contains `domain/`, `application/`, and `infrastructure/` sub-packages.
3. Review class placements to ensure domain logic resides in `domain/`, use cases in `application/`, and technical details (e.g., web controllers, database repositories) in `infrastructure/`.
4. Identify any classes or interfaces that violate the layering principles (e.g., `infrastructure` classes directly accessing `domain` without `application` mediation, or `domain` classes depending on `infrastructure`).
5. Provide feedback on refactoring opportunities to align with the prescribed structure.

## Output esperado
A project structure that consistently follows the `domain/application/infrastructure` layering within each bounded context, promoting maintainability.

## Source refs (project)
- structure_definition
