---
name: skill-testability-scalability
description: "Promote practices that ensure generated code is easily testable and scalable, aligning with the project's architectural goals of high throughput and resilience."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [light]
---

# Testability and Scalability Design Review

## Objetivo
Promote practices that ensure generated code is easily testable and scalable, aligning with the project's architectural goals of high throughput and resilience.

## Trigger
on code generation or modification

## Inputs
- Java source code
- Test classes (JUnit 5.10)
- Architectural design documents

## Procedimiento
1. Review code for clear separation of concerns, making units of code easy to isolate and test (e.g., dependency injection).
2. Verify the presence of unit, integration, and component tests using JUnit 5.10.
3. Assess design choices for scalability, such as stateless services, asynchronous processing, and efficient resource management.
4. Ensure that components are designed to handle high volumes (e.g., 1,500 transactions/second) without becoming bottlenecks.

## Output esperado
Code is designed for high testability and scalability, supporting project performance and reliability goals.

## Source refs (project)
- domain_definition
- tech_stack
