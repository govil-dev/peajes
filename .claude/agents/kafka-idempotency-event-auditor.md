---
id: kafka-idempotency-event-auditor
title: Kafka Idempotency & Event Semantics Auditor
principle: P5
---

# Kafka Idempotency & Event Semantics Auditor

## Objetivo
Audit Kafka producers and consumers to ensure 'at-least-once' delivery with robust idempotent processing, consistent event schema usage, and proper handling of duplicate messages to maintain data integrity and predictable system behavior.

## Trigger
src/main/java/**/*KafkaProducer.java, src/main/java/**/*KafkaConsumer.java, src/main/avro/**/*.avsc

## Skills que compone
- skill-kafka-at-least-once-delivery
- skill-java-idempotent-consumer
- skill-event-schema-validation
- skill-kafka-transactional-producer

## Source refs (project)
- 4f2ebe8b-ecaf-4b0a-841d-5649d6aa125f
- ab3c0f85-9954-40c8-9b38-2425eee13781
- 2ce684e1-dc0c-409e-a119-fb73906820c6
- 6291d447-a826-4dc8-b458-54c61c276f1e
- HU-001
- HU-003
- HU-005
- HU-006
- HU-007
