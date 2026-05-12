---
name: skill-kafka-idempotent-consumer-design
description: "Design and implement Kafka consumers with idempotency logic to ensure that events are processed exactly once, even if received multiple times, using unique identifiers for deduplication."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Kafka Idempotent Consumer Design

## Objetivo
Design and implement Kafka consumers with idempotency logic to ensure that events are processed exactly once, even if received multiple times, using unique identifiers for deduplication.

## Trigger
on Kafka consumer creation or modification

## Inputs
- Kafka consumer code
- Event schemas
- Business logic for event processing

## Procedimiento
1. Identify all Kafka consumer implementations that process critical events.
2. Ensure each event payload includes a unique identifier (e.g., `passId`, `externalReferenceId`, `transactionId`).
3. Implement a deduplication mechanism (e.g., using a transactional database, Redis, or a dedicated idempotency store) to track processed unique identifiers.
4. Before processing an event, check if its unique identifier has already been processed.
5. If already processed, return the result of the original processing without re-executing the business logic.

## Output esperado
Kafka consumer code that correctly handles duplicate messages and ensures idempotent processing.

## Source refs (project)
- kafka-event-delivery--idempotency
- event-delivery-semantics
- idempotent-event-processing
- idempotent-consumer
- HU-001
- HU-003
- HU-005
- HU-006
- HU-007
