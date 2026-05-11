---
name: skill-spring-security-oauth2-rbac
description: "Configure Spring Security OAuth2 Resource Server to integrate with Google Workspace for OIDC authentication, map Google groups to internal roles, and enforce role-based access control (RBAC), including mandatory 2FA for privileged roles."
metadata:
  framework_principle: P5
  enforcement_mode: verify
  criticality_level: [standard]
---

# Spring Security OAuth2 Role-Based Access

## Objetivo
Configure Spring Security OAuth2 Resource Server to integrate with Google Workspace for OIDC authentication, map Google groups to internal roles, and enforce role-based access control (RBAC), including mandatory 2FA for privileged roles.

## Trigger
on security configuration changes or new endpoint creation

## Inputs
- Spring Security configuration files
- Google Workspace OIDC client details
- Role definitions and access policies

## Procedimiento
1. Set up Spring Security OAuth2 Resource Server to validate JWTs issued by Google Workspace.
2. Define a mechanism to map Google Workspace groups (e.g., 'peajes-ops-admins') to internal application roles (e.g., `OPS_ADMIN`).
3. Implement method-level or URL-based security expressions to restrict access based on assigned roles.
4. Verify that Google Workspace policies enforce 2FA for privileged users (e.g., `FinanceAnalyst`) before issuing JWTs.
5. Configure JWT TTL and refresh token strategies for user sessions.

## Output esperado
Secure application endpoints with correct RBAC, OIDC integration, and 2FA enforcement.

## Source refs (project)
- HU-011
- PCI DSS Nivel 4: Acceso a datos de tarjeta restringido por roles (FinanceAnalyst con 2FA obligatorio).
