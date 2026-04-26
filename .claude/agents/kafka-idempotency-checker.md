---
id: kafka-idempotency-checker
title: Kafka Event Idempotency Checker
principle: P5
---

# Kafka Event Idempotency Checker

## Objetivo
Verify that Kafka producers ensure 'at-least-once' delivery for critical events and that consumers implement robust deduplication logic using unique identifiers to guarantee idempotent processing of messages, preventing duplicate operations.

## Skills que compone
- skill-kafka-producer-config
- skill-idempotent-consumer-design
- skill-event-deduplication-logic
- skill-at-least-once-delivery-guarantee

## Source refs (project)
- coding_standard:4f2ebe8b-ecaf-4b0a-841d-5649d6aa125f
- coding_standard:ab3c0f85-9954-40c8-9b38-2425eee13781
- coding_standard:2ce684e1-dc0c-409e-a119-fb73906820c6
- design_pattern:6291d447-a826-4dc8-b458-54c61c276f1e
