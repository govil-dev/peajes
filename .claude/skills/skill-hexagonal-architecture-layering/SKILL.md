---
name: skill-hexagonal-architecture-layering
description: "Verify that code changes respect the hexagonal architecture layers (domain, application, infrastructure/adapters) to maintain strict separation of concerns and allow for testability and portability."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Hexagonal Architecture Layering Enforcement

## Objetivo
Verify that code changes respect the hexagonal architecture layers (domain, application, infrastructure/adapters) to maintain strict separation of concerns and allow for testability and portability.

## Trigger
on code change

## Inputs
- Java source code
- Project package structure

## Procedimiento
1. Examine package structure and class dependencies to ensure 'domain' layer has no dependencies on 'application' or 'infrastructure'.
2. Validate that 'application' layer depends only on 'domain' and defines ports, but not adapters.
3. Confirm 'infrastructure' adapters implement ports defined in 'application' and depend on external technologies.
4. Identify any 'leaky abstractions' where infrastructure details are exposed in higher layers.

## Output esperado
Report on architectural violations with specific class and package references, and suggested refactorings.

## Source refs (project)
- structure_definition:c8b37e3b-df2a-4677-9bfc-7c2ba273aabc
