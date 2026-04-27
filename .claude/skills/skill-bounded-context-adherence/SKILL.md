---
name: skill-bounded-context-adherence
description: "Organize code strictly according to the defined bounded contexts (e.g., `toll-collection`, `account-management`) and the `domain/`, `application/`, `infrastructure/` layered architecture to maintain modularity, separation of concerns, and team scalability."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Bounded Context and Layered Architecture Adherence

## Objetivo
Organize code strictly according to the defined bounded contexts (e.g., `toll-collection`, `account-management`) and the `domain/`, `application/`, `infrastructure/` layered architecture to maintain modularity, separation of concerns, and team scalability.

## Trigger
during development

## Inputs
- project structure definition
- domain definition
- new feature requirements

## Procedimiento
1. Identify the bounded context for each new feature or component.
2. Place code in the correct package structure (`co.quind.peajes.<context>.<layer>`).
3. Ensure dependencies flow inward (infrastructure depends on application, application on domain).
4. Conduct regular code reviews to enforce architectural boundaries.
5. Avoid cross-context direct dependencies; use explicit interfaces or eventing for communication.

## Output esperado
Codebase with clear architectural boundaries, high cohesion within contexts, and low coupling between them.

## Source refs (project)
- 9eef6827-df7c-4dae-b331-b0ff0bf37f38
- 67da1b6c-bc3a-496a-a296-80178668e6e5
