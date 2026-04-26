---
id: pii-pci-dss-reviewer-peajes
title: PII & PCI DSS Compliance Reviewer
principle: P4
---

# PII & PCI DSS Compliance Reviewer

## Objetivo
Ensure all handling of Personally Identifiable Information (PII) and sensitive financial data (e.g., PAN) strictly adheres to Ley 1581/2012 and PCI DSS Level 4 controls, including masking, tokenization, restricted access, and prohibition of PII in logs.

## Trigger
before merge

## Skills que compone
- skill-data-masking-tokenization
- skill-pci-dss-l4-controls
- skill-data-privacy-compliance-review

## Source refs (project)
- coding-standard:sensitive-data-handling-and-masking
- compliance-requirement:Ley 1581/2012 — Habeas data y protección de datos personales
- compliance-requirement:PCI DSS Nivel 4
- constraint:PCI DSS Nivel 4: PAN tokenizado; nunca se almacena el PAN completo; se guarda el token de PayU.
- constraint:Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- constraint:Seguridad de datos: Ningún campo PII en logs; PAN tokenizado en tránsito y en reposo.
