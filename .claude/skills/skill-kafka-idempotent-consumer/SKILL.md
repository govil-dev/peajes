---
name: skill-kafka-idempotent-consumer
description: "Design and implement Kafka consumers to process events exactly once, preventing duplicate operations and ensuring data consistency, especially for critical transactions like account recharges or toll passes."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Implement Idempotent Kafka Event Consumers

## Objetivo
Design and implement Kafka consumers to process events exactly once, preventing duplicate operations and ensuring data consistency, especially for critical transactions like account recharges or toll passes.

## Trigger
on code change in Kafka consumer modules

## Inputs
- Kafka consumer code
- Event schemas

## Procedimiento
1. Identify critical events that require idempotent processing (e.g., `AccountRecharged`, `TransactionAuthorized`).
2. Extract a unique identifier (e.g., `passId`, `externalReferenceId`) from each incoming event.
3. Before processing, check if the unique identifier has already been processed and recorded.
4. If processed, return the original result or log as a duplicate without re-executing the business logic. If not, process and record the identifier.
5. Ensure the recording of processed identifiers is atomic with the business operation.

## Output esperado
Kafka consumers correctly handle duplicate messages, ensuring business operations are applied only once.

## Source refs (project)
- 4f2ebe8b-ecaf-4b0a-841d-5649d6aa125f
- ab3c0f85-9954-40c8-9b38-2425eee13781
- 2ce684e1-dc0c-409e-a119-fb73906820c6
- 6291d447-a826-4dc8-b458-54c61c276f1e
- HU-001:business_rules:1
- HU-003:business_rules:2
- HU-005:acceptance_criteria:5
