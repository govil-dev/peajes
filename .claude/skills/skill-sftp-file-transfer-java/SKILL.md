---
name: skill-sftp-file-transfer-java
description: "Implement secure and reliable SFTP file transfer capabilities in Java for exchanging files with external partners (e.g., Acquiring Bank), including error handling and retry mechanisms."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Secure SFTP File Transfer in Java

## Objetivo
Implement secure and reliable SFTP file transfer capabilities in Java for exchanging files with external partners (e.g., Acquiring Bank), including error handling and retry mechanisms.

## Trigger
during development

## Inputs
- Acquiring Bank SFTP specifications
- File formats (e.g., ISO 20022)
- Security requirements

## Procedimiento
1. Use a robust Java SFTP client library (e.g., JSch or Apache Commons VFS with SFTP support).
2. Configure SFTP connections with appropriate authentication (e.g., private key, username/password) and host key verification.
3. Implement file upload and download operations, ensuring data integrity during transfer.
4. Incorporate resilience patterns such as retries with exponential backoff for transient network issues or server unavailability.
5. Handle common SFTP exceptions (e.g., connection errors, authentication failures, file not found).
6. Ensure sensitive files are encrypted at rest before transfer and deleted securely after successful transmission.
7. Log SFTP transfer attempts, successes, and failures for auditing purposes, without exposing sensitive file content.
8. Write integration tests to simulate SFTP server interactions and verify transfer reliability.

## Output esperado
Reliable and secure SFTP file transfer functionality for critical financial data exchange.

## Source refs (project)
- Integraciones externas fijas: ANI (API REST propietaria, no existe OpenAPI spec, se integra vía adaptador con circuit breaker); Banco adquirente (SFTP + ISO 20022); PayU (SDK Java oficial)
- HU-007
- HU-008
