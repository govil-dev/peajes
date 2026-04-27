---
id: resilience-enforcer-webflux
title: WebFlux Resilience Enforcer
principle: P5
---

# WebFlux Resilience Enforcer

## Objetivo
Ensure all external service integrations, especially those using Spring WebFlux, implement required resilience patterns such as timeouts, circuit breakers, retries, and fallbacks to handle transient failures and service unavailability.

## Trigger
before code review

## Skills que compone
- skill-webflux-resilience-patterns
- skill-external-integration-circuit-breaker
- skill-reactive-programming-resilience

## Source refs (project)
- coding-standard:external-service-integration-resilience
- coding-standard:robust-external-service-integration
- design-pattern:retry-pattern
- design-pattern:fallback-pattern
- constraint:Integraciones externas fijas: ANI (API REST propietaria, no existe OpenAPI spec, se integra vía adaptador con circuit breaker)
