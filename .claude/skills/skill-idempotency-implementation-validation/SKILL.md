---
name: skill-idempotency-implementation-validation
description: "Validate that critical operations, especially payment processing and event handling, are implemented with idempotency to prevent duplicate processing and ensure consistent state."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Idempotency Implementation Validation

## Objetivo
Validate that critical operations, especially payment processing and event handling, are implemented with idempotency to prevent duplicate processing and ensure consistent state.

## Trigger
on code change

## Inputs
- Java source code for critical transactions
- Database schema

## Procedimiento
1. Identify operations requiring idempotency (e.g., `UC-RECARGA-SALDO-PSE`).
2. Verify the presence and correct usage of an `externalReferenceId` or similar unique identifier for tracking.
3. Examine the logic to ensure that if an operation with an already processed `externalReferenceId` is received, the system returns the original result without re-executing the side effects.
4. Review database constraints or application-level checks that enforce uniqueness for idempotent keys.

## Output esperado
Confirmation of idempotent implementation or a list of identified vulnerabilities to duplicate processing.

## Source refs (project)
- use_case:fe4645b8-0c14-47ef-adb1-970bfce30bf7
