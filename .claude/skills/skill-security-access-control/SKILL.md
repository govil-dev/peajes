---
name: skill-security-access-control
description: "Verify that access to functionalities and sensitive data is strictly controlled by roles, enforced via OAuth2/OIDC with Google Workspace, and requires 2FA for privileged roles."
metadata:
  framework_principle: P4
  enforcement_mode: block
  criticality_level: [standard]
---

# Role-Based Access Control and 2FA Enforcement

## Objetivo
Verify that access to functionalities and sensitive data is strictly controlled by roles, enforced via OAuth2/OIDC with Google Workspace, and requires 2FA for privileged roles.

## Trigger
on code generation or modification of security configurations or access-controlled features

## Inputs
- Spring Security configuration classes
- Controller and service methods with access control annotations
- Google Workspace configuration documentation

## Procedimiento
1. Review Spring Security configurations to ensure proper integration with OAuth2/OIDC and JWT validation.
2. Verify that internal system roles (e.g., OPS_ADMIN, FINANCE_ANALYST) are correctly mapped from Google Workspace groups.
3. Check that API endpoints and UI components are protected with `@PreAuthorize` or equivalent role-based access control mechanisms.
4. Confirm that Google Workspace policies enforce 2FA for privileged roles, and the system correctly handles tokens issued under these conditions.
5. Ensure sensitive operations (e.g., PCI DSS related data access) are explicitly restricted to roles with mandatory 2FA.

## Output esperado
Access to system resources is securely controlled by roles, with 2FA enforced for privileged users.

## Source refs (project)
- PCI DSS Nivel 4: Acceso a datos de tarjeta restringido por roles (FinanceAnalyst con 2FA obligatorio).
- HU-011
