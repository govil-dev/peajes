---
name: skill-kafka-idempotent-consumer
description: "Design and implement Kafka consumers to process events idempotently, ensuring operations are processed only once even if messages are received multiple times."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Kafka Idempotent Consumer Implementation

## Objetivo
Design and implement Kafka consumers to process events idempotently, ensuring operations are processed only once even if messages are received multiple times.

## Trigger
on-code-change

## Inputs
- Java source code (Kafka consumers)
- Kafka event schemas

## Procedimiento
1. Identify all Kafka consumers handling critical events.
2. Verify that each consumer implements deduplication logic using unique identifiers (e.g., `passId`, `externalReferenceId`).
3. Ensure that upon receiving a duplicate message, the consumer returns the result of the first processing without re-executing the operation.
4. Confirm that 'at-least-once' delivery semantics are correctly handled by consumer idempotency.

## Output esperado
Kafka consumers process events exactly once, preventing data inconsistencies due to duplicates.

## Source refs (project)
- idempotent-event-processing
- event-delivery-semantics
- kafka-event-delivery--idempotency
- idempotent-consumer
- HU-001
