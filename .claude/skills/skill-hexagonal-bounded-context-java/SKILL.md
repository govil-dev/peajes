---
name: skill-hexagonal-bounded-context-java
description: "Organize the codebase into distinct bounded contexts, each following a Hexagonal Architecture (Ports & Adapters) with `domain/`, `application/`, and `infrastructure/` layers, to promote modularity, testability, and maintainability."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [standard]
---

# Structuring Java Projects with Hexagonal Architecture and Bounded Contexts

## Objetivo
Organize the codebase into distinct bounded contexts, each following a Hexagonal Architecture (Ports & Adapters) with `domain/`, `application/`, and `infrastructure/` layers, to promote modularity, testability, and maintainability.

## Trigger
before generation

## Inputs
- Domain definition
- Project structure definition
- Tech stack

## Procedimiento
1. Define clear boundaries for each bounded context (e.g., `toll-collection`, `account-management`, `billing`).
2. Within each bounded context, create `domain/`, `application/`, and `infrastructure/` root packages.
3. Place core business logic and entities in the `domain/` layer, ensuring it is independent of external concerns.
4. Implement use cases and orchestrate domain interactions in the `application/` layer.
5. Develop adapters for external dependencies (databases, messaging, APIs) in the `infrastructure/` layer, implementing interfaces (ports) defined in the `domain/` or `application/` layers.
6. Enforce dependency rules: `infrastructure` depends on `application`, `application` depends on `domain`, `domain` has no outward dependencies.
7. Use Maven modules to separate bounded contexts if appropriate for larger projects.

## Output esperado
A well-structured Java project with clear separation of concerns, high cohesion within contexts, and low coupling between them.

## Source refs (project)
- structure_definition
- domain_definition
