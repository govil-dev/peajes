---
id: financial-dian-compliance-auditor
title: Financial & DIAN Compliance Auditor
principle: P4
---

# Financial & DIAN Compliance Auditor

## Objetivo
Verify financial data representation (BigDecimal, MoneyAmount), data validation rules, and compliance with DIAN electronic invoicing (XML UBL 2.1, CUFE) and PCI DSS L4 requirements for payment data.

## Trigger
src/main/java/**/{{billing,accountmanagement,reconciliation}}/**/*.java

## Skills que compone
- skill-financial-data-type-validation
- skill-dian-invoice-compliance-check
- skill-pci-dss-data-handling-review
- skill-uuid-iso8601-format-enforcement

## Source refs (project)
- HU-003
- HU-005
- HU-006
- HU-007
- HU-008
- financial-value-representation
- financial-data-type-representation
- data-validation-and-constraints
- data-validation--error-handling
- PCI DSS Nivel 4
- Resolución DIAN 000042/2020
- identifier-format-uuid
- timestamp-format-iso-8601
