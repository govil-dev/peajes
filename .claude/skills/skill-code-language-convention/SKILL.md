---
name: skill-code-language-convention
description: "Ensure all code artifacts (package names, class names, method names, variable names, event names) use English, while business-oriented Javadoc comments use Spanish, for consistent readability and documentation."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [light]
---

# Enforce Code Language Conventions

## Objetivo
Ensure all code artifacts (package names, class names, method names, variable names, event names) use English, while business-oriented Javadoc comments use Spanish, for consistent readability and documentation.

## Trigger
on any code submission

## Inputs
- Source code files
- Javadoc comments

## Procedimiento
1. Review new and modified code for naming conventions: English for all code identifiers.
2. Check Javadoc comments for business logic descriptions to be written in Spanish.
3. Utilize IDE inspections or static analysis tools to flag potential language inconsistencies.
4. Provide guidance on when to use English vs. Spanish in documentation and comments.

## Output esperado
Code adheres to the specified language conventions, improving readability and maintainability.

## Source refs (project)
- Idioma del código: Inglés para nombres de paquete, clases, métodos, eventos. Español en comentarios Javadoc orientados al negocio
