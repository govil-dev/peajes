---
name: skill-structure-code-by-bounded-context
description: "Organize code within the `co.quind.peajes` root package, separating bounded contexts into sub-packages and adhering to `domain/`, `application/`, and `infrastructure/` layering within each context."
metadata:
  framework_principle: P1
  enforcement_mode: instruct
  criticality_level: [light]
---

# Structure Code by Bounded Context and Layers

## Objetivo
Organize code within the `co.quind.peajes` root package, separating bounded contexts into sub-packages and adhering to `domain/`, `application/`, and `infrastructure/` layering within each context.

## Trigger
on creating new modules or components

## Inputs
- Project structure definition
- Bounded context definitions

## Procedimiento
1. Establish `co.quind.peajes` as the root package.
2. Create distinct sub-packages for each bounded context (e.g., `co.quind.peajes.tollcollection`, `co.quind.peajes.accountmanagement`).
3. Within each bounded context package, create `domain/`, `application/`, and `infrastructure/` sub-packages.
4. Place domain models, aggregates, and repositories in `domain/`.
5. Place use cases and application services in `application/`.
6. Place web controllers, database adapters, and external service clients in `infrastructure/`.
7. Ensure dependencies flow inward (infrastructure depends on application, application depends on domain).

## Output esperado
A well-organized codebase that clearly separates concerns by bounded context and architectural layer, enhancing maintainability and scalability.

## Source refs (project)
- 9eef6827-df7c-4dae-b331-b0ff0bf37f38
- structure_definition
- domain_definition
