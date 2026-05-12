---
name: skill-code-language-consistency
description: "Verify that code adheres to the specified language conventions: English for package, class, method, and event names, and Spanish for business-oriented Javadoc comments."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [light]
---

# Code Language and Documentation Consistency

## Objetivo
Verify that code adheres to the specified language conventions: English for package, class, method, and event names, and Spanish for business-oriented Javadoc comments.

## Trigger
on code generation or modification

## Inputs
- Java source code

## Procedimiento
1. Review package, class, method, and variable names for English consistency.
2. Check Javadoc comments for business-related explanations and ensure they are in Spanish.
3. Identify any mixed-language usage within the same code element (e.g., English method name with English Javadoc for business logic).

## Output esperado
Code and documentation consistently follow the defined language conventions.

## Source refs (project)
- Idioma del código: Inglés para nombres de paquete, clases, métodos, eventos. Español en comentarios Javadoc orientados al negocio.
