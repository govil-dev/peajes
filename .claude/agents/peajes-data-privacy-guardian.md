---
id: peajes-data-privacy-guardian
title: Peajes Data & Privacy Guardian
principle: P4
---

# Peajes Data & Privacy Guardian

## Objetivo
Verify data handling practices, PII protection (LPR images, PAN tokenization), transaction idempotency, and audit logging comply with project standards, legal requirements (Ley 1581/2012), and PostgreSQL data integrity best practices.

## Trigger
src/main/java/io/quind/peajes/**/*.java

## Skills que compone
- skill-pii-redaction-lpr
- skill-idempotency-check
- skill-audit-logging-compliance
- skill-r2dbc-data-integrity

## Source refs (project)
- a08d2aa3-7139-4b9b-8347-5c0d3640c290
- fe4645b8-0c14-47ef-adb1-970bfce30bf7
- infrastructure_definition:8acb0625-9e4f-4042-9ede-4148f7b9baf2
