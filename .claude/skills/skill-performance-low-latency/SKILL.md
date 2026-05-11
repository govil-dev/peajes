---
name: skill-performance-low-latency
description: "Guide code generation and review to meet strict low-latency requirements for critical paths, leveraging reactive programming, caching, and efficient data access."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Performance Optimization for Low Latency

## Objetivo
Guide code generation and review to meet strict low-latency requirements for critical paths, leveraging reactive programming, caching, and efficient data access.

## Trigger
on code generation or modification of performance-critical components

## Inputs
- Java source code for critical paths
- Database schema and query definitions
- Caching configurations (Redis)
- Performance test results

## Procedimiento
1. Identify critical paths with strict latency SLOs (e.g., toll authorization <150ms, balance query <100ms).
2. Review code for potential blocking operations or inefficient algorithms that could impact latency.
3. Verify the use of reactive programming (Spring WebFlux, R2DBC) for non-blocking I/O.
4. Check for effective caching strategies (e.g., Redis with appropriate TTLs) for frequently accessed, non-volatile data.
5. Analyze database queries for performance bottlenecks and suggest optimizations (e.g., indexing, query tuning).

## Output esperado
Code is optimized to meet low-latency requirements for critical operations, leveraging appropriate architectural patterns.

## Source refs (project)
- domain_definition
- HU-001
- HU-004
- HU-012
