---
name: skill-standard-tech-stack-usage
description: "Ensure all development strictly adheres to the mandated technology stack (Java 21, Spring Boot 3.3, WebFlux, R2DBC, Kafka, Maven, JUnit 5) for consistency, maintainability, and predictable behavior across all services."
metadata:
  framework_principle: P5
  enforcement_mode: block
  criticality_level: [standard]
---

# Mandatory Technology Stack Adherence

## Objetivo
Ensure all development strictly adheres to the mandated technology stack (Java 21, Spring Boot 3.3, WebFlux, R2DBC, Kafka, Maven, JUnit 5) for consistency, maintainability, and predictable behavior across all services.

## Trigger
during development

## Inputs
- project tech stack definition
- architectural constraints

## Procedimiento
1. Use specified language and framework versions for all new and modified code.
2. Configure Maven `pom.xml` files to enforce correct dependencies and versions.
3. Utilize specified testing framework (JUnit 5) for all tests.
4. Conduct peer reviews to verify adherence to the tech stack.
5. Update development environments and CI/CD pipelines to reflect the mandatory stack.

## Output esperado
A codebase where all components consistently use the defined technology stack.

## Source refs (project)
- 2103332c-ad51-40c9-9e1c-b225264803a6
- Stack técnico mandatorio: No negociable para el MVP. Todo código generado por asistentes de IA debe respetar estas versiones.
