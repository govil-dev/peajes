---
name: skill-sftp-external-integration-management
description: "Develop and manage secure SFTP integrations for file transfers with external partners (e.g., Acquiring Bank), including robust error handling, retry mechanisms, and secure credential management."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# SFTP External Integration Management

## Objetivo
Develop and manage secure SFTP integrations for file transfers with external partners (e.g., Acquiring Bank), including robust error handling, retry mechanisms, and secure credential management.

## Trigger
on SFTP integration development or modification

## Inputs
- SFTP client library usage
- Integration service code
- Credential management configurations

## Procedimiento
1. Implement SFTP client logic for sending and receiving files (e.g., ISO 20022 liquidation files).
2. Configure retry mechanisms with exponential backoff for SFTP operations to handle transient network issues.
3. Implement comprehensive error handling for SFTP failures, including logging and alerting (e.g., `SFTP_DELIVERY_FAILED`).
4. Ensure SFTP credentials are securely stored and accessed (e.g., using GCP Secret Manager).
5. Verify file integrity after transfer (e.g., checksums) where applicable.

## Output esperado
Secure and resilient SFTP integration code with proper error handling and retry logic.

## Source refs (project)
- Integraciones externas fijas: Banco adquirente (SFTP + ISO 20022)
- HU-007
- HU-008
