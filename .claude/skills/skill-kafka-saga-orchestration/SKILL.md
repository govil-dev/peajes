---
name: skill-kafka-saga-orchestration
description: "Design and implement complex, distributed business transactions (sagas) using Kafka for choreography, ensuring eventual consistency and providing compensation mechanisms for failures."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Orchestrating Event-Driven Sagas with Kafka

## Objetivo
Design and implement complex, distributed business transactions (sagas) using Kafka for choreography, ensuring eventual consistency and providing compensation mechanisms for failures.

## Trigger
during development

## Inputs
- Business process definitions
- Kafka infrastructure definition
- Bounded context interactions

## Procedimiento
1. Identify multi-service business processes that require transactional integrity (e.g., daily reconciliation batch closure).
2. Break down the saga into a sequence of local transactions, each publishing an event upon completion.
3. Define Kafka topics for each event in the saga flow.
4. Implement consumers for each event, triggering the next step in the saga or a compensation action.
5. Design compensation logic for each step to revert changes in case of failure in a subsequent step.
6. Utilize correlation IDs (e.g., MDC) to trace saga execution across services.
7. Implement idempotency for all saga participants to handle duplicate events.
8. Write integration tests to verify successful saga completion and compensation flows under various failure scenarios.

## Output esperado
Robust, eventually consistent distributed transactions that handle failures gracefully and maintain data integrity across multiple services.

## Source refs (project)
- HU-007
- infrastructure_definition
