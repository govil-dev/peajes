---
name: skill-rest-api-contract-adherence
description: "Validate that REST API endpoints adhere to the defined contracts, including paths, HTTP methods, request/response bodies, query parameters, and appropriate HTTP status codes for success and error scenarios."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# REST API Contract Adherence

## Objetivo
Validate that REST API endpoints adhere to the defined contracts, including paths, HTTP methods, request/response bodies, query parameters, and appropriate HTTP status codes for success and error scenarios.

## Trigger
on code change

## Inputs
- Java source code for REST controllers
- API documentation (if available)
- Use case acceptance criteria

## Procedimiento
1. Compare implemented API endpoints against the documented API contract (e.g., OpenAPI/Swagger specification if available).
2. Verify correct HTTP methods (GET, POST, PUT, DELETE) are used for their intended actions.
3. Check request and response body structures, ensuring all required fields are present and data types match.
4. Validate the use of appropriate HTTP status codes for success (e.g., 200 OK, 201 Created) and specific error conditions (e.g., 400 Bad Request, 401 Unauthorized, 404 Not Found, 409 Conflict).
5. Ensure consistent error response formats across all endpoints.

## Output esperado
Report detailing any deviations from the API contract, including incorrect status codes or response structures.

## Source refs (project)
- infrastructure_definition:8acb0625-9e4f-4042-9ede-4148f7b9baf2
- use_case:a65808d3-cf05-4f7f-bab1-8edd769dbc75
- use_case:a08d2aa3-7139-4b9b-8347-5c0d3640c290
