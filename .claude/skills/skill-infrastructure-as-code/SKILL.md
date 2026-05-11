---
name: skill-infrastructure-as-code
description: "Guide the use of Terraform for managing infrastructure, ensuring consistency, reproducibility, and version control of all infrastructure components on GCP and Confluent Cloud."
metadata:
  framework_principle: P5
  enforcement_mode: instruct
  criticality_level: [light]
---

# Infrastructure as Code (Terraform) Adherence

## Objetivo
Guide the use of Terraform for managing infrastructure, ensuring consistency, reproducibility, and version control of all infrastructure components on GCP and Confluent Cloud.

## Trigger
on infrastructure changes or new service deployments

## Inputs
- Terraform configuration files (`.tf` files)
- CI/CD pipeline definitions for infrastructure deployment

## Procedimiento
1. Verify that all infrastructure changes are defined in Terraform configuration files.
2. Ensure that Terraform modules are used for reusable and standardized infrastructure patterns.
3. Check for proper state management and remote state storage (e.g., GCS backend).
4. Review Terraform plans for unintended changes or resource drift before application.
5. Advise on best practices for Terraform code organization, naming conventions, and variable management.

## Output esperado
Infrastructure is managed entirely through Terraform, ensuring consistency, auditability, and rapid deployment.

## Source refs (project)
- infrastructure_definition
