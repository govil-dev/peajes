---
name: skill-pii-minimal-logging-enforcement
description: "Ensure that Personally Identifiable Information (PII), such as LPR images, account numbers, or other sensitive data, is never logged, stored, or exposed unnecessarily, adhering to Ley 1581/2012."
metadata:
  framework_principle: P4
  enforcement_mode: block
  criticality_level: [standard]
---

# PII Minimal Logging Enforcement

## Objetivo
Ensure that Personally Identifiable Information (PII), such as LPR images, account numbers, or other sensitive data, is never logged, stored, or exposed unnecessarily, adhering to Ley 1581/2012.

## Trigger
on code change

## Inputs
- Java source code
- Logging configurations
- Data models

## Procedimiento
1. Scan log configurations and code for direct logging of PII fields (e.g., `lprPhotoUrl`, account IDs, names).
2. Verify that sensitive data is masked, tokenized, or redacted before logging or displaying in non-secure contexts.
3. Check for adherence to data retention policies for PII and secure storage practices (e.g., Cloud Storage for LPR images, not database logs).
4. Confirm that PII is only accessed and processed by authorized components and roles.

## Output esperado
Report detailing any instances of PII leakage in logs or insecure handling, with remediation steps.

## Source refs (project)
- use_case:a08d2aa3-7139-4b9b-8347-5c0d3640c290
