---
name: skill-tech-stack-compliance
description: "Verify that all new and modified code adheres to the mandatory technology stack and specified versions (Java 21, Spring Boot 3.3, WebFlux, R2DBC, Kafka)."
metadata:
  framework_principle: P5
  enforcement_mode: block
  criticality_level: [standard]
---

# Standardized Technology Stack Adherence

## Objetivo
Verify that all new and modified code adheres to the mandatory technology stack and specified versions (Java 21, Spring Boot 3.3, WebFlux, R2DBC, Kafka).

## Trigger
on-code-change

## Inputs
- Project `pom.xml`
- Java source code
- Project documentation

## Procedimiento
1. Inspect `pom.xml` or build configuration files for correct dependency versions (Spring Boot 3.3.x, Java 21).
2. Review code for usage of WebFlux reactive programming paradigms where appropriate.
3. Confirm that R2DBC is used for reactive database interactions.
4. Check for correct Kafka client library usage and configuration.
5. Ensure no unauthorized or deprecated technologies are introduced.

## Output esperado
The codebase consistently uses the defined technology stack, ensuring maintainability and predictable behavior.

## Source refs (project)
- standardized-technology-stack
- 2103332c-ad51-40c9-9e1c-b225264803a6
- Stack técnico mandatorio
