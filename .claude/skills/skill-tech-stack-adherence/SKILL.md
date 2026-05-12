---
name: skill-tech-stack-adherence
description: "Ensure all new and modified code strictly adheres to the mandated technology stack (Java 21, Spring Boot 3.3, WebFlux, R2DBC, Kafka), preventing fragmentation and ensuring maintainability."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Adhere to Standardized Technology Stack

## Objetivo
Ensure all new and modified code strictly adheres to the mandated technology stack (Java 21, Spring Boot 3.3, WebFlux, R2DBC, Kafka), preventing fragmentation and ensuring maintainability.

## Trigger
on dependency changes or new module creation

## Inputs
- Project build files (pom.xml)
- New code contributions
- Dependency reports

## Procedimiento
1. Review `pom.xml` or `build.gradle` to verify all dependencies and their versions align with the approved tech stack.
2. Ensure new libraries or frameworks are not introduced without explicit architectural approval.
3. Verify that generated code (e.g., by AI assistants) respects the specified language and framework versions.
4. Conduct regular dependency audits to identify and rectify non-compliant components.

## Output esperado
The project's technology stack remains consistent and compliant with the defined standards.

## Source refs (project)
- 2103332c-ad51-40c9-9e1c-b225264803a6
- Stack técnico mandatorio: No negociable para el MVP. Todo código generado por asistentes de IA debe respetar estas versiones.
