---
id: peajes-resilience-eventing-guardian
title: Peajes Resilience & Eventing Guardian
principle: P5
---

# Peajes Resilience & Eventing Guardian

## Objetivo
Verify that external service integrations implement robust resilience patterns (timeouts, circuit breakers, retries, fallbacks) and that Kafka event producers/consumers ensure 'at-least-once' delivery with idempotent processing.

## Trigger
on file_change_pattern: **/*(Integration|Client|Producer|Consumer)Service.java, **/*(Resilience|Kafka)Config.java, **/*(Retry|CircuitBreaker|Fallback)Aspect.java

## Skills que compone
- skill-resilience-pattern-enforcement
- skill-kafka-delivery-semantics-check
- skill-idempotent-consumer-validation

## Source refs (project)
- 1a573d41-f7e9-42c1-8693-3da003102e62
- fa28a475-5d69-4b7c-b9b0-09fd60a69654
- f014f09a-8fca-442a-ab78-b98d5c032581
- 9cb93807-f4b4-4c76-86c4-dade1c32d08e
- 4f2ebe8b-ecaf-4b0a-841d-5649d6aa125f
- ab3c0f85-9954-40c8-9b38-2425eee13781
- 2ce684e1-dc0c-409e-a119-fb73906820c6
- 6291d447-a826-4dc8-b458-54c61c276f1e
- HU-001
- HU-003
- HU-007
- HU-010
