---
name: skill-r2dbc-reactive-db
description: "Implement non-blocking and reactive database interactions using R2DBC with PostgreSQL to fully support the reactive programming paradigm of Spring WebFlux and achieve high concurrency and scalability."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Reactive Database Interaction with R2DBC

## Objetivo
Implement non-blocking and reactive database interactions using R2DBC with PostgreSQL to fully support the reactive programming paradigm of Spring WebFlux and achieve high concurrency and scalability.

## Trigger
during development

## Inputs
- database schema
- data access requirements
- Spring Data R2DBC documentation

## Procedimiento
1. Configure R2DBC connections for PostgreSQL.
2. Use Spring Data R2DBC repositories for reactive data access operations.
3. Ensure all database operations return Reactor types (Mono, Flux).
4. Handle database transactions reactively.
5. Optimize database queries for reactive execution.

## Output esperado
Efficient, non-blocking data access layer fully integrated with the reactive application stack.

## Source refs (project)
- 95c8da1e-7634-459b-9a59-5da350337a5f
- 3dbba330-77b6-4c3c-a6b9-8f0b25a0c38e
