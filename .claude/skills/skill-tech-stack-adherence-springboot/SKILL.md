---
name: skill-tech-stack-adherence-springboot
description: "Ensure all generated and developed code strictly adheres to the mandated technology stack (Java 21, Spring Boot 3.3.x, WebFlux, Maven, JUnit 5) for consistency and maintainability."
metadata:
  framework_principle: P5
  enforcement_mode: warn
  criticality_level: [standard]
---

# Adhering to Standardized Spring Boot Tech Stack

## Objetivo
Ensure all generated and developed code strictly adheres to the mandated technology stack (Java 21, Spring Boot 3.3.x, WebFlux, Maven, JUnit 5) for consistency and maintainability.

## Trigger
before generation

## Inputs
- Project `pom.xml`
- Tech stack definition
- Codebase

## Procedimiento
1. Verify that `pom.xml` configurations specify the correct Maven, Java, Spring Boot, and JUnit versions.
2. Use Spring WebFlux for all reactive programming paradigms.
3. Ensure R2DBC is used for reactive database interactions where applicable.
4. Avoid introducing new libraries or frameworks not explicitly part of the approved stack without prior architectural review.
5. Regularly review dependencies to ensure they align with the specified versions and do not introduce conflicts.
6. Configure IDEs and build tools to enforce the specified language and runtime versions.

## Output esperado
A codebase that is fully consistent with the defined technology stack, promoting maintainability and predictable behavior.

## Source refs (project)
- 2103332c-ad51-40c9-9e1c-b225264803a6
- Stack técnico mandatorio: No negociable para el MVP. Todo código generado por asistentes de IA debe respetar estas versiones.
- tech_stack
