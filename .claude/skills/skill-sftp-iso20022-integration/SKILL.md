---
name: skill-sftp-iso20022-integration
description: "Securely integrate with external SFTP services for exchanging financial files (e.g., ISO 20022 liquidation files), handling file generation, transmission, retries, and error conditions robustly."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Secure SFTP and ISO 20022 Integration

## Objetivo
Securely integrate with external SFTP services for exchanging financial files (e.g., ISO 20022 liquidation files), handling file generation, transmission, retries, and error conditions robustly.

## Trigger
on code change in reconciliation service or SFTP integration modules

## Inputs
- Reconciliation service code
- SFTP client configuration
- ISO 20022 file generation logic

## Procedimiento
1. Use a robust and secure SFTP client library for file transfers.
2. Implement retry logic with exponential backoff for SFTP operations to handle transient network issues.
3. Ensure secure credential management for SFTP access.
4. Develop logic for generating ISO 20022 compliant XML files for bank liquidations.
5. Implement error handling and alerting for SFTP transmission failures, ensuring manual re-sending capabilities.

## Output esperado
Financial files are securely generated and transmitted via SFTP, with robust error handling and retry mechanisms.

## Source refs (project)
- Integraciones externas fijas: Banco adquirente (SFTP + ISO 20022)
- HU-007:acceptance_criteria:4
- HU-007:exceptions:2
- HU-008:business_rules:8
- HU-008:exceptions:6
