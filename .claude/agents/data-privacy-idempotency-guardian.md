---
id: data-privacy-idempotency-guardian
title: Data Privacy & Event Idempotency Guardian
principle: P4
---

# Data Privacy & Event Idempotency Guardian

## Objetivo
Ensure Personally Identifiable Information (PII) is handled according to privacy laws (Ley 1581/2012) including masking in logs and storage, and validate that Kafka event consumers implement robust idempotency logic.

## Trigger
src/main/java/**/kafka/consumer/**/*.java, src/main/java/**/domain/model/**/*.java, src/main/resources/logback.xml

## Skills que compone
- skill-pii-masking-logging-audit
- skill-kafka-idempotent-consumer-validation

## Source refs (project)
- HU-001
- HU-002
- HU-003
- HU-009
- HU-012
- sensitive-data-handling-and-masking
- idempotent-event-processing
- event-delivery-semantics
- kafka-event-delivery--idempotency
- idempotent-consumer
- Ley 1581/2012 — Habeas data y protección de datos personales
- Seguridad de datos
- Logging
