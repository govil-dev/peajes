# Contexto de arquitectura

> Generado por Guardian Suite. Actualizar con: `agentic context`

## Stack tecnológico
Spring Boot 3.3.x + Spring WebFlux / Java / Java 21 / Maven 3.9.x / JUnit 5.10

## Estándares de codificación
External Service Integration Resilience: Interactions with external services must implement resilience patterns such as timeouts, circuit breakers, retries, fallbacks, and fire-and-forget for non-critical asynchronous calls. [language=general] [category=inferred]
Data Validation & Error Handling: String fields must adhere to specified length limits. Numeric fields (e.g., balances) must enforce non-negativity, throwing specific exceptions (e.g., `InsufficientBalanceException`) on violation. [language=general] [category=inferred]
Financial Data Type Representation: Monetary amounts and balances must be represented as strings with decimal precision. Internally, BigDecimal with scale 2 should be used, and MoneyAmount value objects must preserve currency. [language=general] [category=inferred]
Kafka Event Delivery & Idempotency: Kafka producers must ensure 'at-least-once' delivery for critical events, requiring consumers to be designed with idempotency to handle potential duplicate messages. [language=general] [category=inferred]
Event Delivery Semantics: Kafka producers must ensure 'at-least-once' delivery for critical events, requiring consumers to be designed with idempotency to handle potential duplicate messages. [language=general] [category=inferred]
Standardized Technology Stack: All services must adhere to a consistent technology stack, primarily Java 21, Spring Boot 3.3, WebFlux, R2DBC, and Kafka, for development and deployment. [language=general] [category=inferred]
Sensitive Data Handling and Masking: Personally Identifiable Information (PII) and sensitive financial data (e.g., PAN, license plates, LPR images) must be handled according to data privacy laws (Ley 1581/2012) and PCI DSS L4 controls, including masking, tokenization, or strict avoidance of storage. [language=general] [category=inferred]
Data Validation and Constraints: String fields must adhere to specified length limits (e.g., max 500 characters for free text). Numeric fields representing balances must enforce non-negativity, throwing specific exceptions (e.g., `InsufficientBalanceException`) when business rules are violated. [language=general] [category=inferred]
Idempotent Event Processing: Consumers must implement deduplication logic for events using unique identifiers (e.g., `passId`, `TollPassId`) to ensure operations are processed only once, returning the result of the first processing for subsequent identical requests. [language=general] [category=inferred]
Robust External Service Integration: Interactions with external services must implement resilience patterns such as timeouts, circuit breakers, retries, fallbacks, and fire-and-forget for non-critical asynchronous calls. [language=general] [category=inferred]
Enumerated String Values: Fields with a predefined set of options (e.g., resolution, failureReason, reason, status) should use specific string literals, implying the use of enums or constants in code. [language=general] [category=inferred]
Financial Value Representation: Monetary amounts and balances (e.g., amount, balanceAfter, refundAmount) must be represented as strings with decimal precision. Internally, BigDecimal with scale 2 should be used, and MoneyAmount value objects must preserve currency. [language=general] [category=inferred]
Timestamp Format (ISO 8601): All timestamp fields (e.g., openedAt, detectedAt, authorizedAt) must be represented as ISO 8601 formatted strings. [language=general] [category=inferred]
Identifier Format (UUID): All unique identifiers (e.g., disputeId, accountId, transactionId) must be represented as UUID strings. [language=general] [category=inferred]

## Patrones de diseño
Value Object: Explicitly defined for immutable data types like `GovernanceScore`, `Severity`, and `CommitSha`, with validation performed in their constructors (`__post_init__`). [category=general]
Retry Pattern: Applied to external service calls (e.g., DIAN proveedor tecnológico) to handle transient failures by re-attempting operations. [category=general]
Fallback Pattern: Used in external service integrations (e.g., ANI API) to provide alternative behavior or cached data when the primary service is unavailable. [category=general]
Idempotent Consumer: Implemented for event consumers (e.g., TollPassRegistered) to ensure events are processed only once, even if received multiple times, using unique identifiers like passId for deduplication. [category=general]

## AI Attribution via MCP

After each significant Edit or Write you make to a code file in this workspace, call the MCP tool `tlm_record_ai_attributions` from the Tech Lead Manager server with the file path, line range you authored, your generator name and model. This is how the project tracks AI Contribution Rate, retention and governance metrics.

To obtain the `X-API-Key` header value for the TLM MCP server, run `agentic mcp-key --print` (the key was persisted in `~/.agentic/profile.json` after `agentic login`). Use `agentic mcp-key --export` to print an `export TLM_MCP_API_KEY=<key>` line you can `eval` in your shell or paste into your AI coder's MCP config.

The tool accepts:

- `project_code` (required): use the value `PEAJES-CO-01` (already set in `.agentic.json`).
- `commit_sha` (optional): leave `null` while editing — TLM reconciles to the real SHA when you push the PR.
- `attributions` (required, array): one item per range you generated, with:
  - `file_path` (relative to the repo root)
  - `line_start`, `line_end` (1-indexed, inclusive)
  - `generator` (e.g. `"claude-code"`, `"cursor"`, `"copilot"`)
  - `model` (e.g. `"claude-opus-4-7"`, `"gpt-4o"`)
  - `agent_node` (optional, for LangGraph flows)

Skip non-code files (`.md`, `.json`, `.yml`, build configs, images, lock files) — they are not relevant to AI contribution metrics. Only attribute changes to source code in the languages of the project's tech stack.

Example call (single edit, 3 lines authored):

```json
{
  "project_code": "PEAJES-CO-01",
  "commit_sha": null,
  "attributions": [
    {
      "file_path": "src/main/java/com/example/Foo.java",
      "line_start": 12,
      "line_end": 14,
      "generator": "claude-code",
      "model": "claude-opus-4-7"
    }
  ]
}
```

## Dominio
The business domain is 'Peajes Colombia' (Toll Collection in Colombia), focusing on electronic toll systems. The primary goal is to process high volumes of vehicle transactions (up to 1,500 transactions per second) with strict low-latency requirements (<150ms p95) for charge authorization, which directly impacts physical barrier operation and traffic flow. The system must integrate with various external dependencies, including RFID antennas, bank acquirers, the National Infrastructure Agency (ANI), payment gateways (PayU), and multiple concessionaires, all with potentially variable and unpredictable latencies. Key functionalities are divided into several bounded contexts: `toll-collection` (registering passes, validating tags, authorizing charges), `account-management` (managing accounts, balances, and tags), `billing` (generating electronic invoices compliant with DIAN regulations), `incident-management` (handling user disputes), and `reconciliation` (comparing system transactions with bank statements and concessionaire reports). The architecture aims to support a small development team in managing this complex domain while ensuring testability, scalability, and resilience against external dependency failures.

## Infraestructura
The infrastructure is primarily hosted on Google Cloud Platform, leveraging Cloud Run for scalable API services and Cloud SQL PostgreSQL for relational databases. Confluent Cloud provides a robust Kafka messaging backbone for inter-service communication and event streaming. External integrations utilize various protocols including HTTP REST and SFTP, all managed with Terraform for Infrastructure as Code.

## Quality gate
Governance score mínimo: **60/100**
Validar con: `agentic check`
