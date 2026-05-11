---
name: skill-bounded-context-adherence
description: "Ensure code changes respect the boundaries and responsibilities of defined bounded contexts (e.g., `toll-collection`, `account-management`, `billing`), promoting modularity and maintainability."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [light]
---

# Bounded Context Adherence and Separation of Concerns

## Objetivo
Ensure code changes respect the boundaries and responsibilities of defined bounded contexts (e.g., `toll-collection`, `account-management`, `billing`), promoting modularity and maintainability.

## Trigger
on code generation or modification

## Inputs
- Java package structure
- Service and repository interfaces
- Domain event definitions

## Procedimiento
1. Identify the bounded context to which the change belongs.
2. Verify that code modifications are contained within the appropriate package structure (e.g., `co.quind.peajes.tollcollection`).
3. Check that direct dependencies between bounded contexts are minimized and mediated through explicit interfaces or events, rather than direct class coupling.
4. Ensure that business logic specific to one context does not leak into another (e.g., `account-management` is the sole authority for balance modification).

## Output esperado
Code changes respect bounded context boundaries, promoting a clear separation of concerns and modular architecture.

## Source refs (project)
- domain_definition
- structure_definition
- HU-003
