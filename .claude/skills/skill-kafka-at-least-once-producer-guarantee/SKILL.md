---
name: skill-kafka-at-least-once-producer-guarantee
description: "Configure Kafka producers to ensure 'at-least-once' delivery for critical events, handling potential network issues and retries to prevent message loss."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Kafka At-Least-Once Producer Guarantee

## Objetivo
Configure Kafka producers to ensure 'at-least-once' delivery for critical events, handling potential network issues and retries to prevent message loss.

## Trigger
on Kafka producer creation or modification

## Inputs
- Kafka producer configurations
- Event publishing code
- Business requirements for event delivery guarantees

## Procedimiento
1. Identify all Kafka producer implementations for critical events.
2. Configure producer `acks` to `all` (or `-1`).
3. Set `retries` to a sufficiently high value (e.g., `Integer.MAX_VALUE` or a specific number with exponential backoff).
4. Ensure `max.in.flight.requests.per.connection` is set to 1 if message ordering is critical, or a higher value if ordering is not strictly required per partition.
5. Implement error callbacks for producers to log and alert on persistent delivery failures.

## Output esperado
Kafka producer code configured for 'at-least-once' delivery, with appropriate error handling.

## Source refs (project)
- kafka-event-delivery--idempotency
- event-delivery-semantics
- HU-001
- HU-002
- HU-003
- HU-005
- HU-006
- HU-007
- HU-009
- HU-010
