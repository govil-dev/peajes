---
name: skill-financial-transaction-integrity
description: "Ensure financial transactions maintain integrity, with correct balance updates, precise monetary representation, and auditable trails."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Financial Transaction Integrity Verification

## Objetivo
Ensure financial transactions maintain integrity, with correct balance updates, precise monetary representation, and auditable trails.

## Trigger
on code generation or modification of financial transaction logic

## Inputs
- Java code for financial operations
- Database schema for accounts and transactions
- Domain event definitions related to financial movements

## Procedimiento
1. Verify that all monetary amounts and balances are represented using `BigDecimal` with appropriate scale and `MoneyAmount` value objects to preserve currency.
2. Confirm that account balance modifications are atomic and occur only through the `account-management` bounded context.
3. Review transaction flows (e.g., toll charges, account recharges, refunds) to ensure correct debit/credit logic and prevent double-spending or over-crediting.
4. Check for audit trails on all financial movements, linking them to specific transactions and events.

## Output esperado
Financial transactions are processed with high integrity, ensuring accurate balances and auditable records.

## Source refs (project)
- financial-value-representation
- financial-data-type-representation
- HU-003
- HU-001
