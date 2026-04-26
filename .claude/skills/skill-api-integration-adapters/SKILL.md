---
name: skill-api-integration-adapters
description: "Develop robust and isolated adapters for integrating with diverse external APIs (e.g., ANI REST API, Banco adquirente SFTP, PayU SDK), encapsulating external system complexities and ensuring architectural flexibility for future changes or additional reporting needs."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# External API Integration with Adapters

## Objetivo
Develop robust and isolated adapters for integrating with diverse external APIs (e.g., ANI REST API, Banco adquirente SFTP, PayU SDK), encapsulating external system complexities and ensuring architectural flexibility for future changes or additional reporting needs.

## Trigger
during development

## Inputs
- external API specifications (e.g., ANI, PayU, ISO 20022)
- integration requirements
- architectural constraints

## Procedimiento
1. Design an interface for each external service interaction within the application layer.
2. Implement concrete adapter classes in the infrastructure layer responsible for translating domain requests to external API calls and vice-versa.
3. Handle external API specific protocols (REST, SFTP, SDK) within the adapter.
4. Implement error handling and retry logic specific to each external integration.
5. Ensure adapters can be easily swapped or extended without impacting core domain logic.

## Output esperado
Well-defined and isolated integration adapters that abstract external system details from the core application.

## Source refs (project)
- Integraciones externas fijas: ANI (API REST propietaria, no existe OpenAPI spec, se integra vía adaptador con circuit breaker); Banco adquirente (SFTP + ISO 20022); PayU (SDK Java oficial)
- SuperTransporte — Reportes regulatorios
