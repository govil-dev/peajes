---
name: skill-consistent-technology-stack-adherence
description: "Ensure all new development and modifications strictly adhere to the mandated technology stack (Java 21, Spring Boot 3.3, WebFlux, R2DBC, Kafka) to maintain consistency and simplify maintenance."
metadata:
  framework_principle: P5
  enforcement_mode: block
  criticality_level: [standard]
---

# Consistent Technology Stack Adherence

## Objetivo
Ensure all new development and modifications strictly adhere to the mandated technology stack (Java 21, Spring Boot 3.3, WebFlux, R2DBC, Kafka) to maintain consistency and simplify maintenance.

## Trigger
on new project setup, dependency addition, or major code change

## Inputs
- Project `pom.xml` or build files
- New code dependencies
- Developer proposals for new technologies

## Procedimiento
1. Review project dependencies and build configurations (e.g., `pom.xml` for Maven).
2. Verify that all new libraries and frameworks are compatible with the mandated stack.
3. Ensure Java language features used are compatible with Java 21.
4. Discourage the introduction of alternative technologies or versions not explicitly approved.
5. Provide guidance on using Spring Boot 3.3 and WebFlux best practices.

## Output esperado
Codebase consistently using the defined technology stack, without unauthorized deviations.

## Source refs (project)
- standardized-technology-stack
- Stack técnico mandatorio: No negociable para el MVP. Todo código generado por asistentes de IA debe respetar estas versiones.
