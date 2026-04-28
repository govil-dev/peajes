---
name: skill-pci-dss-payment-handling-java
description: "Implement payment processing logic that adheres to PCI DSS Level 4 requirements, specifically for tokenization of PANs, secure transmission, and restricted access to sensitive data."
metadata:
  framework_principle: P4
  enforcement_mode: verify
  criticality_level: [standard]
---

# PCI DSS Level 4 Compliant Payment Handling in Java

## Objetivo
Implement payment processing logic that adheres to PCI DSS Level 4 requirements, specifically for tokenization of PANs, secure transmission, and restricted access to sensitive data.

## Trigger
during development

## Inputs
- PCI DSS Level 4 requirements
- PayU Java SDK documentation
- Security policies

## Procedimiento
1. Ensure that the full Primary Account Number (PAN) is never stored within the system; only PayU-provided tokens should be retained.
2. All transmission of card data must occur over HTTPS/TLS 1.3.
3. Integrate with the official PayU Java SDK for payment gateway interactions.
4. Implement role-based access control (RBAC) to restrict access to payment data, requiring 2FA for privileged roles like `FinanceAnalyst`.
5. Ensure audit logs are generated for all access to payment data, with a minimum retention of 1 year.
6. Collaborate with security teams for quarterly vulnerability scans (ASV external).
7. Conduct security reviews of code handling payment data to ensure compliance.

## Output esperado
Payment processing functionality that securely handles cardholder data, minimizing risk and ensuring compliance with PCI DSS.

## Source refs (project)
- PCI DSS Nivel 4
- c6de6d45-2b64-4524-b225-9bee71273e1b
- HU-003
