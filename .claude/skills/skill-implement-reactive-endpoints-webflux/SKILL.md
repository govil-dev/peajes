---
name: skill-implement-reactive-endpoints-webflux
description: "Develop high-performance, non-blocking REST endpoints using Spring WebFlux to efficiently handle high transaction volumes (up to 1,500 TPS) and meet strict low-latency requirements (<150ms p95)."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Implement Reactive Endpoints with Spring WebFlux

## Objetivo
Develop high-performance, non-blocking REST endpoints using Spring WebFlux to efficiently handle high transaction volumes (up to 1,500 TPS) and meet strict low-latency requirements (<150ms p95).

## Trigger
on developing new API endpoints

## Inputs
- API specifications
- Performance requirements (TPS, latency)
- Spring WebFlux documentation

## Procedimiento
1. Use Spring WebFlux annotations (`@RestController`, `@GetMapping`, `@PostMapping`, etc.) for defining API endpoints.
2. Ensure all service methods and repository interactions are non-blocking and return `Mono` or `Flux`.
3. Utilize reactive programming operators (e.g., `map`, `flatMap`, `zip`) for data transformation and composition.
4. Integrate with reactive data access (R2DBC) for database operations.
5. Implement appropriate error handling for reactive streams.
6. Conduct performance testing to ensure latency and throughput targets are met.

## Output esperado
Non-blocking, high-throughput REST APIs capable of handling peak loads with low latency, as verified by performance metrics.

## Source refs (project)
- 95c8da1e-7634-459b-9a59-5da350337a5f
- tech_stack
- domain_definition
