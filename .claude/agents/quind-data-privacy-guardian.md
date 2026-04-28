---
id: quind-data-privacy-guardian
title: Data Privacy & Security Guardian
principle: P4
---

# Data Privacy & Security Guardian

## Objetivo
Ensure strict adherence to data privacy laws (Ley 1581/2012) and PCI DSS Level 4 controls, specifically by enforcing PII masking, PAN tokenization, secure data transmission, and restricted access to sensitive financial data across all system components and logs.

## Skills que compone
- skill-pii-masking
- skill-data-tokenization
- skill-pci-dss-compliance
- skill-privacy-by-design

## Source refs (project)
- coding_standards:c6de6d45-2b64-4524-b225-9bee71273e1b
- constraints:PCI DSS Nivel 4: PAN tokenizado; nunca se almacena el PAN completo; se guarda el token de PayU.
- constraints:Seguridad de datos: Ningún campo PII en logs; PAN tokenizado en tránsito y en reposo.
- constraints:Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.
- compliance_requirements:Ley 1581/2012 — Habeas data y protección de datos personales
- compliance_requirements:PCI DSS Nivel 4
- use_cases:e841da87-af8a-4d88-8073-e8a57999b00f
- use_cases:daa86dca-52b1-46f8-a7f5-aa739ee07461
- use_cases:803ceb66-a758-4e6a-bb0d-5ff27bdd2085
