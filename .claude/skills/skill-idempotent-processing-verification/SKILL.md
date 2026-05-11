---
name: skill-idempotent-processing-verification
description: "Ensure that event consumers and critical write operations are designed to be idempotent, processing messages or requests only once, even if received multiple times."
metadata:
  framework_principle: P2
  enforcement_mode: verify
  criticality_level: [standard]
---

# Idempotent Event and Operation Processing

## Objetivo
Ensure that event consumers and critical write operations are designed to be idempotent, processing messages or requests only once, even if received multiple times.

## Trigger
on code generation or modification of event consumers or state-changing APIs

## Inputs
- Kafka consumer implementations
- Spring WebFlux controller methods for write operations
- Database schema with unique constraints

## Procedimiento
1. Identify all Kafka event consumers and critical API endpoints that modify state.
2. Verify that each consumer/endpoint uses a unique identifier (e.g., `passId`, `externalReferenceId`, `transactionId`) to detect and prevent duplicate processing.
3. Confirm that upon detecting a duplicate, the system returns the result of the first processing without re-executing the side effects.
4. Review the persistence layer to ensure unique constraints or conditional updates support idempotency.

## Output esperado
Event consumers and critical operations correctly handle duplicate messages/requests without unintended side effects.

## Source refs (project)
- idempotent-event-processing
- kafka-event-delivery--idempotency
- idempotent-consumer
- HU-001
- HU-003
- HU-005
- HU-006
- HU-007
- event-delivery-semantics
