---
name: skill-spring-security-oauth2-oidc
description: "Implement and verify secure authentication and authorization using OAuth2/OIDC with Google Workspace and Spring Security, including role-based access control and 2FA enforcement for privileged roles."
metadata:
  framework_principle: P4
  enforcement_mode: block
  criticality_level: [standard]
---

# OAuth2/OIDC and Spring Security Implementation

## Objetivo
Implement and verify secure authentication and authorization using OAuth2/OIDC with Google Workspace and Spring Security, including role-based access control and 2FA enforcement for privileged roles.

## Trigger
on-code-change

## Inputs
- Java source code (Spring Security configuration)
- Security policies
- Google Workspace configuration

## Procedimiento
1. Verify that Spring Security OAuth2 Resource Server is correctly configured to validate JWTs from Google Workspace.
2. Ensure internal system roles (OPS_ADMIN, FINANCE_ANALYST) are correctly mapped from Google Workspace groups.
3. Check that access to resources and functionalities is strictly enforced based on assigned roles.
4. Confirm that privileged roles require 2FA, as managed and applied by Google Workspace policies.
5. Review JWT and refresh token TTLs for appropriate session management.

## Output esperado
Authentication and authorization mechanisms are secure, role-based, and compliant with 2FA requirements.

## Source refs (project)
- HU-011
- PCI DSS Nivel 4: Acceso a datos de tarjeta restringido por roles (FinanceAnalyst con 2FA obligatorio).
