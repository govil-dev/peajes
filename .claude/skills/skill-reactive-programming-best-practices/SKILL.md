---
name: skill-reactive-programming-best-practices
description: "Guide the use of Spring WebFlux for non-blocking, reactive programming, ensuring efficient resource utilization and adherence to best practices for high-throughput operations."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [light]
---

# Spring WebFlux Reactive Programming Best Practices

## Objetivo
Guide the use of Spring WebFlux for non-blocking, reactive programming, ensuring efficient resource utilization and adherence to best practices for high-throughput operations.

## Trigger
on code generation or modification of reactive components

## Inputs
- Spring WebFlux controller and service classes
- R2DBC repository implementations

## Procedimiento
1. Review controller and service methods to ensure they return reactive types (e.g., `Mono`, `Flux`) and avoid blocking calls.
2. Check for proper use of operators (e.g., `map`, `flatMap`, `zip`) to compose reactive streams efficiently.
3. Verify that database interactions leverage R2DBC for non-blocking access.
4. Ensure error handling within reactive pipelines is robust and non-blocking.
5. Advise on thread pool management and context propagation in reactive flows.

## Output esperado
Reactive code is idiomatic, non-blocking, and follows Spring WebFlux best practices.

## Source refs (project)
- tech_stack
- domain_definition
