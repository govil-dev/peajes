---
name: skill-develop-idempotent-kafka-consumers
description: "Design and implement Kafka consumers to process events exactly once, even if messages are received multiple times, by using unique identifiers for deduplication."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Develop Idempotent Kafka Consumers

## Objetivo
Design and implement Kafka consumers to process events exactly once, even if messages are received multiple times, by using unique identifiers for deduplication.

## Trigger
on Kafka consumer development

## Inputs
- Kafka event schemas
- Unique event identifiers
- Consumer processing logic

## Procedimiento
1. Identify critical events that require 'at-least-once' delivery semantics and idempotent processing.
2. Ensure event messages contain a unique identifier (e.g., `passId`, `TollPassId`).
3. Implement a deduplication mechanism in the consumer, typically by storing processed event IDs in a persistent store (e.g., Redis, database) before processing.
4. Before processing an event, check if its unique ID has already been processed. If so, return the result of the first processing or skip.
5. Ensure the entire processing logic, including state changes and side effects, is idempotent.

## Output esperado
Kafka consumers that reliably process events only once, preventing duplicate operations and maintaining data consistency.

## Source refs (project)
- 4f2ebe8b-ecaf-4b0a-841d-5649d6aa125f
- ab3c0f85-9954-40c8-9b38-2425eee13781
- 2ce684e1-dc0c-409e-a119-fb73906820c6
- kafka-event-delivery--idempotency
- event-delivery-semantics
- idempotent-event-processing
- idempotent-consumer
- 6291d447-a826-4dc8-b458-54c61c276f1e
