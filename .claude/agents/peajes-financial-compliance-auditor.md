---
id: peajes-financial-compliance-auditor
title: Peajes Financial Compliance Auditor
principle: P2
---

# Peajes Financial Compliance Auditor

## Objetivo
Ensure all financial transactions, data representations, and billing processes strictly adhere to Colombian DIAN regulations, PCI DSS Level 4, and internal financial data standards, with verifiable acceptance criteria.

## Trigger
src/main/java/co/quind/peajes/{{billing,accountmanagement,reconciliation}}/**/*.java, src/main/resources/application.yml

## Skills que compone
- skill-financial-data-type-validation
- skill-dian-xml-compliance
- skill-pci-dss-tokenization-check
- skill-iso20022-liquidation-validation
- skill-reconciliation-logic-verification

## Source refs (project)
- 58c84ffe-71f3-456c-927d-1d2647c2b26b
- 280e17f5-f799-4390-abec-9f5156240673
- c6de6d45-2b64-4524-b225-9bee71273e1b
- PCI DSS Nivel 4
- Resolución DIAN 000042/2020
- HU-003
- HU-005
- HU-006
- HU-007
- HU-008
