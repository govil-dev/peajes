---
id: spring-webflux-resilience-guardian
title: Spring WebFlux Resilience Guardian
principle: P5
---

# Spring WebFlux Resilience Guardian

## Objetivo
Verify that all external service integrations and asynchronous operations implement robust resilience patterns (timeouts, circuit breakers, retries, fallbacks) and Kafka event processing ensures at-least-once delivery with consumer idempotency, maintaining high performance and availability.

## Trigger
src/main/java/co/quind/peajes/**/*.java, src/main/resources/application.yml, pom.xml

## Skills que compone
- skill-spring-webflux-reactive-patterns
- skill-resilience-pattern-validation
- skill-kafka-idempotency-check
- skill-external-integration-timeout-check
- skill-observability-metric-integration

## Source refs (project)
- 1a573d41-f7e9-42c1-8693-3da003102e62
- fa28a475-5d69-4b7c-b9b0-09fd60a69654
- 4f2ebe8b-ecaf-4b0a-841d-5649d6aa125f
- ab3c0f85-9954-40c8-9b38-2425eee13781
- 2ce684e1-dc0c-409e-a119-fb73906820c6
- f014f09a-8fca-442a-ab78-b98d5c032581
- 9cb93807-f4b4-4c76-86c4-dade1c32d08e
- 6291d447-a826-4dc8-b458-54c61c276f1e
- HU-001
- HU-003
- HU-012
