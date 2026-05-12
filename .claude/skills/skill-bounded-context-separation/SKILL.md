---
name: skill-bounded-context-separation
description: "Ensure code changes respect the defined bounded contexts and the `domain/application/infrastructure` layered architecture, promoting modularity and separation of concerns."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Bounded Context and Layered Architecture Adherence

## Objetivo
Ensure code changes respect the defined bounded contexts and the `domain/application/infrastructure` layered architecture, promoting modularity and separation of concerns.

## Trigger
on-code-change

## Inputs
- Java source code
- Project structure documentation

## Procedimiento
1. Verify that code within each bounded context (`toll-collection`, `account-management`, `billing`, etc.) is self-contained and minimizes direct dependencies on other contexts.
2. Check that packages follow the `co.quind.peajes.<context>.domain`, `co.quind.peajes.<context>.application`, and `co.quind.peajes.<context>.infrastructure` structure.
3. Ensure domain logic resides in the `domain` layer, application services in `application`, and external integrations/persistence in `infrastructure`.
4. Identify and flag any 'leaky abstractions' or cross-context dependencies that violate the boundaries.

## Output esperado
The codebase maintains a clear separation of concerns according to bounded contexts and layered architecture.

## Source refs (project)
- domain_definition
- structure_definition
