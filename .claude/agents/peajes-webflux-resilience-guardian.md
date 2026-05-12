---
id: peajes-webflux-resilience-guardian
title: Peajes WebFlux Resilience Guardian
principle: P5
---

# Peajes WebFlux Resilience Guardian

## Objetivo
Verify that Spring WebFlux applications and external service integrations implement robust resilience patterns (timeouts, circuit breakers, retries, fallbacks) and meet low-latency performance requirements, especially for Kafka event processing.

## Trigger
src/main/java/**/*.java

## Skills que compone
- skill-peajes-webflux-resilience-patterns
- skill-peajes-kafka-delivery-assurance
- skill-peajes-performance-slo-check
- skill-peajes-reactive-programming-review

## Source refs (project)
- coding-standard:robust-external-service-integration
- coding-standard:event-delivery-semantics
- design-pattern:fallback-pattern
- design-pattern:retry-pattern
- constraint:Integraciones externas fijas: ANI (API REST propietaria, no existe OpenAPI spec, se integra vía adaptador con circuit breaker); Banco adquirente (SFTP + ISO 20022); PayU (SDK Java oficial)
- domain-definition:67da1b6c-bc3a-496a-a296-80178668e6e5
- use-case:e841da87-af8a-4d88-8073-e8a57999b00f
- use-case:139c8fb5-861d-48a3-ad86-53bda2e52038
- use-case:48c72c87-fde4-41bb-b2bb-12ba688f00ed
- use-case:ef3b1cbe-a796-4d47-b043-4887eae0441d
- use-case:803ceb66-a758-4e6a-bb0d-5ff27bdd2085
- use-case:b638153c-2516-43a0-a304-5e4e4c895790
