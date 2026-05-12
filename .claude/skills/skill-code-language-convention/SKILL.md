---
name: skill-code-language-convention
description: "Ensure all code artifacts (packages, classes, methods, events) use English, while Javadoc comments for business context use Spanish, as per project standards."
metadata:
  framework_principle: P5
  enforcement_mode: warn
  criticality_level: [light]
---

# Code Language Convention Enforcement

## Objetivo
Ensure all code artifacts (packages, classes, methods, events) use English, while Javadoc comments for business context use Spanish, as per project standards.

## Trigger
on-code-change

## Inputs
- Java source code

## Procedimiento
1. Review package, class, method, and variable names for adherence to English naming conventions.
2. Inspect Javadoc comments for business-oriented descriptions written in Spanish.
3. Flag any mixed-language usage within code identifiers or non-business Javadoc comments.

## Output esperado
Code adheres to the defined language conventions, improving readability and maintainability.

## Source refs (project)
- Idioma del código: Inglés para nombres de paquete, clases, métodos, eventos. Español en comentarios Javadoc orientados al negocio
