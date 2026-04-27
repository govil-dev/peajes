---
name: skill-reactive-api-development
description: "Develop non-blocking, reactive APIs using Spring WebFlux to meet high throughput (1,500 TPS) and low-latency (<150ms p95) requirements for critical operations like charge authorization."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Reactive API Development with Spring WebFlux

## Objetivo
Develop non-blocking, reactive APIs using Spring WebFlux to meet high throughput (1,500 TPS) and low-latency (<150ms p95) requirements for critical operations like charge authorization.

## Trigger
during development

## Inputs
- API specification
- use case requirements
- performance targets

## Procedimiento
1. Design API endpoints following RESTful principles and reactive patterns.
2. Implement business logic using Reactor types (Mono, Flux) for asynchronous processing.
3. Ensure efficient resource utilization and backpressure handling.
4. Write unit and integration tests for reactive flows using WebTestClient and StepVerifier.

## Output esperado
Reactive API endpoints implemented with Spring WebFlux, passing all performance and functional tests.

## Source refs (project)
- 95c8da1e-7634-459b-9a59-5da350337a5f
- 67da1b6c-bc3a-496a-a296-80178668e6e5
