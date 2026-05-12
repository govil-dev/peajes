---
name: skill-reactive-low-latency
description: "Design and implement critical paths, especially for toll authorization, to meet strict low-latency requirements (<150ms p95) by leveraging Spring WebFlux's reactive programming principles and optimizing data access."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Optimize for Reactive Low-Latency Processing

## Objetivo
Design and implement critical paths, especially for toll authorization, to meet strict low-latency requirements (<150ms p95) by leveraging Spring WebFlux's reactive programming principles and optimizing data access.

## Trigger
on code change in performance-critical paths or new feature implementation

## Inputs
- Critical path service code (e.g., toll-collection-api)
- Database access layer
- Caching configurations

## Procedimiento
1. Prioritize non-blocking I/O operations throughout the critical path using Spring WebFlux's `Mono` and `Flux`.
2. Minimize synchronous calls and blocking operations, especially for database access (using R2DBC) and external service integrations.
3. Implement caching strategies (e.g., Redis with appropriate TTL) for frequently accessed read-heavy data.
4. Profile and benchmark critical flows to identify and eliminate performance bottlenecks.
5. Ensure efficient data serialization/deserialization and network communication.

## Output esperado
Critical operations meet latency SLOs, demonstrating efficient use of reactive programming and optimized resource access.

## Source refs (project)
- domain_definition:content
- HU-001:business_rules:5
- HU-004:acceptance_criteria:2
- HU-012:business_rules:3
