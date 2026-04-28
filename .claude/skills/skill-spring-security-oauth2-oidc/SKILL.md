---
name: skill-spring-security-oauth2-oidc
description: "Implement secure authentication and authorization using Spring Security's OAuth2 Resource Server, integrating with Google Workspace as an OpenID Connect provider, and mapping Google groups to internal roles."
metadata:
  framework_principle: P4
  enforcement_mode: verify
  criticality_level: [standard]
---

# Integrating Spring Security with OAuth2/OIDC for Google Workspace

## Objetivo
Implement secure authentication and authorization using Spring Security's OAuth2 Resource Server, integrating with Google Workspace as an OpenID Connect provider, and mapping Google groups to internal roles.

## Trigger
during development

## Inputs
- Google Workspace configuration
- Spring Security documentation
- Role definitions

## Procedimiento
1. Configure Spring Security to act as an OAuth2 Resource Server, validating JWTs issued by Google Workspace.
2. Set up Google Workspace as the OIDC provider, ensuring proper client registration and scope configuration.
3. Implement custom `GrantedAuthoritiesMapper` to map Google Workspace groups (e.g., 'peajes-ops-admins') to internal application roles (e.g., `OPS_ADMIN`).
4. Apply method-level or URL-based security expressions (`@PreAuthorize`, `hasRole()`) to enforce role-based access control.
5. Ensure Google Workspace policies enforce 2FA for privileged roles, and verify that the system correctly handles tokens from 2FA-enabled users.
6. Configure JWT token TTLs (8 hours) and refresh token strategies (7 days) for session management.
7. Write integration tests for authentication flows, role mapping, and access control enforcement.

## Output esperado
A secure authentication and authorization system that leverages Google Workspace for identity management and enforces granular access control.

## Source refs (project)
- HU-011
- PCI DSS Nivel 4: Acceso a datos de tarjeta restringido por roles (FinanceAnalyst con 2FA obligatorio).
