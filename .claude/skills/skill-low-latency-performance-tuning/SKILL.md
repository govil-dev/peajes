---
name: skill-low-latency-performance-tuning
description: "Identify and optimize code paths to meet strict low-latency requirements (<150ms p95), particularly for high-volume transaction processing in the `toll-collection` context."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Low Latency Performance Optimization

## Objetivo
Identify and optimize code paths to meet strict low-latency requirements (<150ms p95), particularly for high-volume transaction processing in the `toll-collection` context.

## Trigger
on-code-change

## Inputs
- Java source code (toll-collection, account-management)
- Performance test results
- Monitoring data

## Procedimiento
1. Analyze critical paths in the `toll-collection` context for potential latency bottlenecks.
2. Verify efficient use of reactive programming constructs in Spring WebFlux to avoid blocking operations.
3. Ensure database queries are optimized and indexed for fast retrieval.
4. Check for appropriate caching strategies (e.g., Redis for account balances) with suitable TTLs.
5. Review external service calls for minimal overhead and asynchronous execution.

## Output esperado
Critical transaction processing paths meet the p95 latency target of <150ms.

## Source refs (project)
- domain_definition
- HU-001
- HU-004
