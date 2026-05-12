---
name: skill-bounded-context-integrity
description: "Ensure that code changes respect the defined bounded contexts (`toll-collection`, `account-management`, `billing`, `incident-management`, `reconciliation`), preventing direct cross-context data access or tight coupling."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Maintain Bounded Context Integrity

## Objetivo
Ensure that code changes respect the defined bounded contexts (`toll-collection`, `account-management`, `billing`, `incident-management`, `reconciliation`), preventing direct cross-context data access or tight coupling.

## Trigger
on code change affecting multiple contexts or new service creation

## Inputs
- Project package structure
- Service interaction diagrams
- Code changes across contexts

## Procedimiento
1. Review package structure to confirm adherence to `co.quind.peajes.<context>` convention.
2. Verify that services only interact with other contexts via explicit, well-defined APIs (Kafka events, REST endpoints).
3. Prevent direct access to another context's internal data models, repositories, or private classes.
4. Ensure domain logic remains within its respective bounded context and does not leak into others.

## Output esperado
Code adheres to bounded context boundaries, promoting modularity, maintainability, and clear separation of concerns.

## Source refs (project)
- structure_definition
- domain_definition
- HU-003:business_rules:1
