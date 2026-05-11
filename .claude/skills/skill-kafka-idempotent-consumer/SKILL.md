---
name: skill-kafka-idempotent-consumer
description: "Confirm that all Kafka consumers for critical events implement robust deduplication logic using unique identifiers to ensure operations are processed exactly once, even if messages are received multiple times."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Kafka Idempotent Consumer Verification

## Objetivo
Confirm that all Kafka consumers for critical events implement robust deduplication logic using unique identifiers to ensure operations are processed exactly once, even if messages are received multiple times.

## Trigger
on code review

## Inputs
- Kafka consumer code
- Database schemas for event tracking
- Redis usage patterns

## Procedimiento
1. Identify all Kafka consumer implementations for critical business events (e.g., `TollPassRegistered`, `AccountRecharged`).
2. Verify the presence of a deduplication mechanism (e.g., storing processed event IDs in a persistent store like Redis or database).
3. Ensure that unique identifiers (e.g., `passId`, `externalReferenceId`) are consistently used for deduplication checks.
4. Review the logic to handle duplicate messages: return the result of the first processing without re-executing the business logic.
5. Check for proper error handling and logging in case of deduplication failures or unexpected states.

## Output esperado
Kafka consumers that reliably process events exactly once, preventing data inconsistencies from duplicate messages.

## Source refs (project)
- kafka-event-delivery--idempotency
- event-delivery-semantics
- idempotent-event-processing
- idempotent-consumer
- HU-001
- HU-003
