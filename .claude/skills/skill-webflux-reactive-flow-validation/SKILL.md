---
name: skill-webflux-reactive-flow-validation
description: "Ensure reactive flows in Spring WebFlux applications adhere to non-blocking principles, proper backpressure handling, and efficient resource utilization, avoiding blocking operations."
metadata:
  framework_principle: P5
  enforcement_mode: warn
  criticality_level: [standard]
---

# Spring WebFlux Reactive Flow Validation

## Objetivo
Ensure reactive flows in Spring WebFlux applications adhere to non-blocking principles, proper backpressure handling, and efficient resource utilization, avoiding blocking operations.

## Trigger
on code change

## Inputs
- Java source code (Spring WebFlux, R2DBC)
- Reactive programming guidelines

## Procedimiento
1. Analyze code for any blocking calls within reactive chains (e.g., `block()`, `Thread.sleep()`, synchronous I/O).
2. Verify proper use of reactive operators for error handling, retries, and timeouts.
3. Assess the use of Schedulers to ensure operations are executed on appropriate threads (e.g., I/O-bound vs. CPU-bound).
4. Confirm that database interactions leverage R2DBC's reactive capabilities without introducing blocking points.

## Output esperado
Report detailing identified blocking operations or inefficient reactive patterns, with recommendations for correction.

## Source refs (project)
- tech_stack:abdc8a1e-f338-4a58-92e0-ee5153a06c49
- structure_definition:c8b37e3b-df2a-4677-9bfc-7c2ba273aabc
