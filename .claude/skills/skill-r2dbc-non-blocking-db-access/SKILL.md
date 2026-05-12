---
name: skill-r2dbc-non-blocking-db-access
description: "Ensure all database interactions leverage R2DBC in a fully non-blocking and reactive manner, preventing any accidental blocking calls that could degrade performance in a WebFlux application."
metadata:
  framework_principle: P5
  enforcement_mode: warn
  criticality_level: [standard]
---

# R2DBC Non-Blocking Database Access

## Objetivo
Ensure all database interactions leverage R2DBC in a fully non-blocking and reactive manner, preventing any accidental blocking calls that could degrade performance in a WebFlux application.

## Trigger
on code change

## Inputs
- Java source code (R2DBC repositories, services)
- Database configuration

## Procedimiento
1. Review all repository and data access layer code for direct or indirect blocking database calls.
2. Verify that all database operations return reactive types (e.g., `Mono`, `Flux`) and are composed using reactive operators.
3. Check for proper connection management and transaction handling within the reactive paradigm.
4. Ensure that custom queries or stored procedure calls are also executed reactively.

## Output esperado
Report confirming non-blocking R2DBC usage or identifying blocking patterns with suggested reactive alternatives.

## Source refs (project)
- tech_stack:abdc8a1e-f338-4a58-92e0-ee5153a06c49
- structure_definition:c8b37e3b-df2a-4677-9bfc-7c2ba273aabc
