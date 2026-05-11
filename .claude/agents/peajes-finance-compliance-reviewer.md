---
id: peajes-finance-compliance-reviewer
title: Peajes Finance & Compliance Reviewer
principle: P4
---

# Peajes Finance & Compliance Reviewer

## Objetivo
Ensure all financial data handling, billing processes, and sensitive data management comply with DIAN regulations, PCI DSS L4, and Ley 1581/2012, maintaining data integrity and privacy.

## Trigger
on file_change_pattern: **/*(Money|Balance|Invoice|CreditNote|Reconciliation|Payment|Transaction)Service.java, **/*(Money|Balance|Invoice|CreditNote|Payment|Transaction)DTO.java, **/*(Money|Balance|Invoice|CreditNote|Payment|Transaction).java, **/*(Security|WebSecurity)Config.java

## Skills que compone
- skill-financial-data-type-validation
- skill-dian-billing-compliance
- skill-pci-dss-data-handling
- skill-pii-masking-verification

## Source refs (project)
- 58c84ffe-71f3-456c-927d-1d2647c2b26b
- 280e17f5-f799-4390-abec-9f5156240673
- c6de6d45-2b64-4524-b225-9bee71273e1b
- Ley 1581/2012 — Habeas data y protección de datos personales
- PCI DSS Nivel 4
- Resolución DIAN 000042/2020 — Facturación electrónica
- HU-003
- HU-005
- HU-006
- HU-007
