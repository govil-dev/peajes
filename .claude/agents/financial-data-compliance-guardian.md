---
id: financial-data-compliance-guardian
title: Financial Data & Compliance Guardian
principle: P4
---

# Financial Data & Compliance Guardian

## Objetivo
Verify that financial data types are correctly implemented using BigDecimal and MoneyAmount value objects, PII is handled securely with masking/tokenization, and all data processing adheres to DIAN and PCI DSS compliance requirements.

## Trigger
src/main/java/**/*.java, src/main/resources/**/*.xml

## Skills que compone
- skill-java-financial-data-modeling
- skill-pii-data-masking
- skill-dian-xml-ubl-validation
- skill-pci-dss-data-handling

## Source refs (project)
- 5388fa7c-fae1-45ab-ab45-820fd2932e9b
- 58c84ffe-71f3-456c-927d-1d2647c2b26b
- c6de6d45-2b64-4524-b225-9bee71273e1b
- 280e17f5-f799-4390-abec-9f5156240673
- 152eb76e-66e2-4e56-9a26-27b7290af8f6
- PCI DSS Nivel 4
- Ley 1581/2012 — Habeas data y protección de datos personales
- Resolución DIAN 000042/2020 — Facturación electrónica
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- HU-003
- HU-005
- HU-006
