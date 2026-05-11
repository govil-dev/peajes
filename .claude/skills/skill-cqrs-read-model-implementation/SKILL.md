---
name: skill-cqrs-read-model-implementation
description: "Implement Command Query Responsibility Segregation (CQRS) by creating dedicated read models (e.g., using Redis cache or separate database views) for efficient and low-latency query operations, decoupled from write models."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [light]
---

# CQRS Read Model Implementation

## Objetivo
Implement Command Query Responsibility Segregation (CQRS) by creating dedicated read models (e.g., using Redis cache or separate database views) for efficient and low-latency query operations, decoupled from write models.

## Trigger
on new query feature development with performance requirements

## Inputs
- Query requirements
- Domain events
- Database schema definitions
- Caching configurations

## Procedimiento
1. Identify high-volume or low-latency query requirements (e.g., `HU-004: Consultar saldo e historial de cobros`).
2. Design a dedicated read model (e.g., a denormalized view in PostgreSQL or a Redis cache).
3. Implement mechanisms to update the read model from domain events (e.g., `AccountRecharged`, `TransactionAuthorized`).
4. Ensure read model updates are eventually consistent with the write model.
5. Configure caching strategies (e.g., Redis TTL of 10s for balance) to meet latency SLOs.

## Output esperado
Dedicated read models and query services that provide low-latency data access, decoupled from transactional write models.

## Source refs (project)
- HU-004
