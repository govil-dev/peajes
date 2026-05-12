---
name: skill-standardized-tech-stack-adherence
description: "Verify that all new code and configurations strictly adhere to the mandatory technology stack (Java 21, Spring Boot 3.3, WebFlux, R2DBC, Kafka)."
metadata:
  framework_principle: P5
  enforcement_mode: block
  criticality_level: [standard]
---

# Standardized Technology Stack Adherence

## Objetivo
Verify that all new code and configurations strictly adhere to the mandatory technology stack (Java 21, Spring Boot 3.3, WebFlux, R2DBC, Kafka).

## Trigger
on code generation or modification

## Inputs
- Project `pom.xml`
- Source code files
- Configuration files

## Procedimiento
1. Review `pom.xml` or build configuration files to ensure correct versions of Java, Spring Boot, and other core dependencies.
2. Check for the use of non-standard libraries or frameworks that are not part of the approved stack.
3. Confirm that reactive programming paradigms (WebFlux) are consistently applied where appropriate for performance-critical components.
4. Validate that database interactions use R2DBC for reactive access where applicable.

## Output esperado
All generated or modified code aligns with the project's standardized technology stack.

## Source refs (project)
- standardized-technology-stack
- 2103332c-ad51-40c9-9e1c-b225264803a6
- Stack técnico mandatorio: No negociable para el MVP. Todo código generado por asistentes de IA debe respetar estas versiones.
- tech_stack
