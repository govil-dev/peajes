---
name: skill-adhere-to-standard-tech-stack
description: "Ensure all development strictly adheres to the mandated technology stack: Java 21, Spring Boot 3.3, Spring WebFlux, R2DBC, Maven, JUnit 5.10, and Kafka."
metadata:
  framework_principle: P5
  enforcement_mode: block
  criticality_level: [standard]
---

# Adhere to Standardized Technology Stack

## Objetivo
Ensure all development strictly adheres to the mandated technology stack: Java 21, Spring Boot 3.3, Spring WebFlux, R2DBC, Maven, JUnit 5.10, and Kafka.

## Trigger
on project setup or dependency addition

## Inputs
- Project `pom.xml`
- Development environment setup
- New library dependencies

## Procedimiento
1. Verify the project's `pom.xml` (Maven) specifies the correct Java, Spring Boot, and other library versions.
2. Use Spring WebFlux for all reactive programming paradigms.
3. Utilize R2DBC for reactive database interactions with PostgreSQL.
4. Ensure Kafka client libraries are compatible with Confluent Cloud.
5. Avoid introducing unapproved libraries or frameworks not part of the standard stack.

## Output esperado
Codebase built exclusively with the approved technology stack, ensuring consistency and maintainability.

## Source refs (project)
- 2103332c-ad51-40c9-9e1c-b225264803a6
- standardized-technology-stack
- Stack técnico mandatorio: No negociable para el MVP. Todo código generado por asistentes de IA debe respetar estas versiones.
- 95c8da1e-7634-459b-9a59-5da350337a5f
