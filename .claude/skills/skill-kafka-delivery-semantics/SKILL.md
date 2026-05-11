---
name: skill-kafka-delivery-semantics
description: "Ensure Kafka producers guarantee 'at-least-once' delivery for critical events and that consumers are designed with idempotency to handle potential duplicate messages."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Kafka Event Delivery Semantics Verification

## Objetivo
Ensure Kafka producers guarantee 'at-least-once' delivery for critical events and that consumers are designed with idempotency to handle potential duplicate messages.

## Trigger
on code generation or modification of Kafka producers or consumers

## Inputs
- Kafka producer and consumer configurations
- Java code for Kafka producers and consumers

## Procedimiento
1. Review Kafka producer configurations to confirm 'at-least-once' delivery semantics (e.g., `acks=all`, retries).
2. Identify critical events that require guaranteed delivery and verify their producer configurations.
3. Cross-reference with `skill-idempotent-processing-verification` to ensure corresponding consumers are idempotent.
4. Check for proper error handling and dead-letter queue (DLQ) mechanisms for consumer failures.

## Output esperado
Kafka event delivery semantics are correctly configured, ensuring critical events are processed reliably and idempotently.

## Source refs (project)
- event-delivery-semantics
- kafka-event-delivery--idempotency
- ab3c0f85-9954-40c8-9b38-2425eee13781
- 4f2ebe8b-ecaf-4b0a-841d-5649d6aa125f
