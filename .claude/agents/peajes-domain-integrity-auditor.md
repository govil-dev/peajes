---
id: peajes-domain-integrity-auditor
title: Peajes Domain Integrity Auditor
principle: P5
---

# Peajes Domain Integrity Auditor

## Objetivo
Ensure all domain models and business logic adhere to specified data validation rules, consistent identifier/timestamp formats, and enumerated string value usage, preserving core business invariants.

## Trigger
on file_change_pattern: **/domain/model/**/*.java, **/application/usecase/**/*.java, **/domain/valueobject/**/*.java, **/domain/event/**/*.java

## Skills que compone
- skill-data-validation-enforcement
- skill-identifier-timestamp-format-check
- skill-value-object-pattern-validation
- skill-business-rule-consistency

## Source refs (project)
- 5388fa7c-fae1-45ab-ab45-820fd2932e9b
- 88f3213d-e945-4cf7-bf2e-c59a9906f9b7
- 6bcca597-0794-4870-ad22-fa067b8bca72
- 6df3d29a-591e-4cda-96f5-548d4507bc88
- 6d1329f3-af63-4331-b3cf-4c83abb1496f
- 152eb76e-66e2-4e56-9a26-27b7290af8f6
- HU-001
- HU-002
- HU-009
