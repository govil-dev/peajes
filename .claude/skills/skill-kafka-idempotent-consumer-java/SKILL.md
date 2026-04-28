---
name: skill-kafka-idempotent-consumer-java
description: "Design and implement Kafka consumers that process messages exactly once, even if duplicates are received, ensuring data consistency and correctness."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Implementing Idempotent Kafka Consumers in Spring Boot

## Objetivo
Design and implement Kafka consumers that process messages exactly once, even if duplicates are received, ensuring data consistency and correctness.

## Trigger
during development

## Inputs
- Kafka event schemas
- Consumer business logic
- Database access layer

## Procedimiento
1. Identify critical events that require idempotent processing (e.g., `AccountRecharged`, `TransactionAuthorized`, `InvoiceIssued`).
2. Ensure event messages include a unique identifier (e.g., `externalReferenceId`, `passId`, `transactionId`) suitable for deduplication.
3. Implement a deduplication mechanism within the consumer logic, typically by checking for the existence of a processed record using the unique identifier before applying state changes.
4. For successful deduplication, return the result of the original processing without re-executing business logic or publishing new events.
5. Write integration tests to verify idempotent behavior under duplicate message scenarios.

## Output esperado
Kafka consumers that correctly handle duplicate messages without side effects, ensuring 'at-least-once' delivery semantics lead to 'exactly-once' processing.

## Source refs (project)
- 4f2ebe8b-ecaf-4b0a-841d-5649d6aa125f
- ab3c0f85-9954-40c8-9b38-2425eee13781
- 2ce684e1-dc0c-409e-a119-fb73906820c6
- 6291d447-a826-4dc8-b458-54c61c276f1e
- HU-003
- HU-005
- HU-001
