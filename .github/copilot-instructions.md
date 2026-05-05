# Instructions para GitHub Copilot

> Instrucciones rich generadas por Guardian Suite (FACTORY-30). No es pointer.

## Stack
Spring Boot 3.3.x + Spring WebFlux / Java / Java 21 / Maven 3.9.x / JUnit 5.10

## Estándares
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

## Dominio + Infraestructura
The business domain is 'Peajes Colombia' (Toll Collection in Colombia), focusing on electronic toll systems. The primary goal is to process high volumes of vehicle transactions (up to 1,500 transactions per second) with strict low-latency requirements (<150ms p95) for charge authorization, which directly impacts physical barrier operation and traffic flow. The system must integrate with various external dependencies, including RFID antennas, bank acquirers, the National Infrastructure Agency (ANI), payment gateways (PayU), and multiple concessionaires, all with potentially variable and unpredictable latencies. Key functionalities are divided into several bounded contexts: `toll-collection` (registering passes, validating tags, authorizing charges), `account-management` (managing accounts, balances, and tags), `billing` (generating electronic invoices compliant with DIAN regulations), `incident-management` (handling user disputes), and `reconciliation` (comparing system transactions with bank statements and concessionaire reports). The architecture aims to support a small development team in managing this complex domain while ensuring testability, scalability, and resilience against external dependency failures.

The infrastructure is primarily hosted on Google Cloud Platform, leveraging Cloud Run for scalable API services and Cloud SQL PostgreSQL for relational databases. Confluent Cloud provides a robust Kafka messaging backbone for inter-service communication and event streaming. External integrations utilize various protocols including HTTP REST and SFTP, all managed with Terraform for Infrastructure as Code.

## Framework Quind
# Framework Quind (v1.0.0)

## core-context-first
Require official, current, and relevant context before generation or modification.

- Identify the source of truth before drafting code or advice.
- Call out missing context explicitly instead of filling gaps with invention.
- Prefer repository artifacts, schemas, tickets, and approved documentation over model memory.

## core-verifiability-required
Require measurable acceptance criteria and evidence expectations before calling work done.

- Translate ambiguous requests into observable criteria before implementation.
- Differentiate functional correctness from smoke validation and quality checks.
- Do not close work without stating what was validated and what remains assumed.

## core-hitl-required
Keep human checkpoints explicit before generation, acceptance, and closure.

- Treat generated output as a proposal, not a decision.
- Ask for or record review confirmation before final acceptance.
- Escalate reinforced changes to stronger human review.

## core-privacy-minimal-disclosure
Use the minimum safe context and refuse to expose secrets, PII, or sensitive assets unnecessarily.

- Prefer synthetic, masked, or minimal samples when context is sensitive.
- Stop and sanitize when secrets, PII, or customer-sensitive content appear.
- Use only approved tools and flows for the current project context.

## core-technical-quality-baseline
Preserve clarity, maintainability, and predictable behavior as baseline quality goals.

- Prefer simple, readable structures over clever but opaque implementation.
- Use automatic validators when they materially improve confidence.
- Keep changes coherent with the language and repository conventions already in use.

## core-framework-operating-model
Anchor every task to the P1-P5 operating model before generating, editing, validating, or closing work.

- Start by classifying the task and locating the source of truth before proposing edits.
- Keep acceptance criteria, human review, privacy, and quality visible throughout the task.
- Escalate rigor with criticity instead of applying one fixed level of formality to every task.
