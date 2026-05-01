---
name: skill-error-exception-mapping-standardization
description: "Validate that application exceptions are consistently mapped to appropriate HTTP status codes and standardized error response bodies, providing clear and actionable feedback to API consumers."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Error and Exception Mapping Standardization

## Objetivo
Validate that application exceptions are consistently mapped to appropriate HTTP status codes and standardized error response bodies, providing clear and actionable feedback to API consumers.

## Trigger
on code change

## Inputs
- Java source code for exception handling
- Use case exception definitions

## Procedimiento
1. Review exception handling mechanisms (e.g., `@ControllerAdvice`, `WebExceptionHandler`) to ensure global consistency.
2. Verify that specific business exceptions (e.g., `ACCOUNT_NOT_FOUND`, `ACCOUNT_INACTIVE`, `Unauthorized Access`) are mapped to their corresponding HTTP status codes (e.g., 404, 409, 401).
3. Check that error responses include a consistent structure, typically with an error code, message, and optionally details.
4. Ensure that internal server errors (5xx) do not expose sensitive internal details to the client.

## Output esperado
Assessment of error mapping consistency and completeness, identifying any non-standard or misleading error responses.

## Source refs (project)
- use_case:a65808d3-cf05-4f7f-bab1-8edd769dbc75
- use_case:a08d2aa3-7139-4b9b-8347-5c0d3640c290
- use_case:fe4645b8-0c14-47ef-adb1-970bfce30bf7
