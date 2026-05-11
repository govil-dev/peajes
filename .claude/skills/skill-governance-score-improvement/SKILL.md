---
name: skill-governance-score-improvement
description: "Provide guidance and actionable steps to improve the project's governance score by adhering to coding standards, compliance requirements, and architectural principles."
metadata:
  framework_principle: P2
  enforcement_mode: instruct
  criticality_level: [light]
---

# Governance Score Improvement Guidance

## Objetivo
Provide guidance and actionable steps to improve the project's governance score by adhering to coding standards, compliance requirements, and architectural principles.

## Trigger
on code review or governance score reports

## Inputs
- Code review findings
- Static analysis reports
- Fitness function results (e.g., `gov-score-30d`, `gov-pass-rate-30d`)

## Procedimiento
1. Analyze recent code changes and identify areas that deviate from established coding standards, design patterns, or compliance requirements.
2. Suggest specific refactorings or additions to align with standards (e.g., adding resilience patterns, improving data validation, masking PII).
3. Recommend improvements to test coverage and quality to increase the governance pass rate.
4. Provide links to relevant documentation (coding standards, compliance requirements) for self-correction.

## Output esperado
Actionable recommendations for developers to improve code quality and compliance, leading to an increased governance score.

## Source refs (project)
- gov-score-30d
- gov-pass-rate-30d
