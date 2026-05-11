---
name: skill-javadoc-business-context-documentation
description: "Write comprehensive Javadoc comments in Spanish for business-oriented methods, classes, and interfaces to provide clear context and understanding for domain experts and new team members."
metadata:
  framework_principle: P5
  enforcement_mode: warn
  criticality_level: [light]
---

# Javadoc Business Context Documentation

## Objetivo
Write comprehensive Javadoc comments in Spanish for business-oriented methods, classes, and interfaces to provide clear context and understanding for domain experts and new team members.

## Trigger
on new business logic implementation or significant code modification

## Inputs
- New or modified business logic code
- Existing code lacking business context documentation

## Procedimiento
1. Identify business-critical classes, interfaces, and public methods.
2. Write Javadoc comments in Spanish, explaining the business purpose, invariants, and side effects.
3. Include references to relevant business rules or use cases where appropriate.
4. Ensure Javadoc is up-to-date with code changes.
5. Maintain English for package, class, and method names as per coding standards.

## Output esperado
Code with clear, concise, and accurate Javadoc comments in Spanish for business-related elements.

## Source refs (project)
- Idioma del código: Inglés para nombres de paquete, clases, métodos, eventos. Español en comentarios Javadoc orientados al negocio.
