---
id: kafka-idempotency-auditor-spring
title: Kafka Idempotency Auditor (Spring)
principle: P2
---

# Kafka Idempotency Auditor (Spring)

## Objetivo
Confirm that Kafka consumers implement robust deduplication logic using unique identifiers to ensure events are processed only once, and that producers ensure 'at-least-once' delivery for critical events.

## Trigger
before deployment

## Skills que compone
- skill-kafka-deduplication-logic
- skill-at-least-once-delivery-verification
- skill-spring-kafka-consumer-design

## Source refs (project)
- coding-standard:kafka-event-delivery--idempotency
- coding-standard:event-delivery-semantics
- coding-standard:idempotent-event-processing
- design-pattern:idempotent-consumer
