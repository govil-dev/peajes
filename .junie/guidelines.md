# Guidelines para JetBrains Junie

> Contenido completo — no es pointer. Generado por Guardian Suite (FACTORY-30).


## Stack tecnológico
Spring Boot 3.3.x + Spring WebFlux / Java / Java 21 / Maven 3.9.x / JUnit 5.10

## Coding standards
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

## Design patterns
Value Object: Explicitly defined for immutable data types like `GovernanceScore`, `Severity`, and `CommitSha`, with validation performed in their constructors (`__post_init__`). [category=general]
Retry Pattern: Applied to external service calls (e.g., DIAN proveedor tecnológico) to handle transient failures by re-attempting operations. [category=general]
Fallback Pattern: Used in external service integrations (e.g., ANI API) to provide alternative behavior or cached data when the primary service is unavailable. [category=general]
Idempotent Consumer: Implemented for event consumers (e.g., TollPassRegistered) to ensure events are processed only once, even if received multiple times, using unique identifiers like passId for deduplication. [category=general]

## Dominio
The business domain is 'Peajes Colombia' (Toll Collection in Colombia), focusing on electronic toll systems. The primary goal is to process high volumes of vehicle transactions (up to 1,500 transactions per second) with strict low-latency requirements (<150ms p95) for charge authorization, which directly impacts physical barrier operation and traffic flow. The system must integrate with various external dependencies, including RFID antennas, bank acquirers, the National Infrastructure Agency (ANI), payment gateways (PayU), and multiple concessionaires, all with potentially variable and unpredictable latencies. Key functionalities are divided into several bounded contexts: `toll-collection` (registering passes, validating tags, authorizing charges), `account-management` (managing accounts, balances, and tags), `billing` (generating electronic invoices compliant with DIAN regulations), `incident-management` (handling user disputes), and `reconciliation` (comparing system transactions with bank statements and concessionaire reports). The architecture aims to support a small development team in managing this complex domain while ensuring testability, scalability, and resilience against external dependency failures.

## Infraestructura
The infrastructure is primarily hosted on Google Cloud Platform, leveraging Cloud Run for scalable API services and Cloud SQL PostgreSQL for relational databases. Confluent Cloud provides a robust Kafka messaging backbone for inter-service communication and event streaming. External integrations utilize various protocols including HTTP REST and SFTP, all managed with Terraform for Infrastructure as Code.

## Constraints
- Presupuesto de infraestructura: ≤ USD 2.500/mes en GCP + Confluent Cloud para ambientes dev + staging + prod durante MVP
- Deadline MVP: Q3 2025 — release a producción con el primer concesionario piloto
- Integraciones externas fijas: ANI (API REST propietaria, no existe OpenAPI spec, se integra vía adaptador con circuit breaker); Banco adquirente (SFTP + ISO 20022); PayU (SDK Java oficial)
- Hardware de estación existente: Antenas RFID (protocolo EPC Gen2), cámaras LPR con API propietaria — no se reemplaza hardware
- Regulación tarifaria: Las tarifas de peaje las fija la ANI — el sistema las carga como configuración, no las calcula
- Equipo: 4 desarrolladores backend + 1 tech lead + 1 QA — no se contrata más personal para MVP
- Idioma del código: Inglés para nombres de paquete, clases, métodos, eventos. Español en comentarios Javadoc orientados al negocio
- Stack técnico mandatorio: No negociable para el MVP. Todo código generado por asistentes de IA debe respetar estas versiones.
- PCI DSS Nivel 4: Aplica cuando se procesan números de tarjeta para recargas (integración PayU).
- PCI DSS Nivel 4: PAN tokenizado; nunca se almacena el PAN completo; se guarda el token de PayU.
- PCI DSS Nivel 4: Transmisión de datos de tarjeta siempre vía HTTPS/TLS 1.3.
- PCI DSS Nivel 4: Acceso a datos de tarjeta restringido por roles (FinanceAnalyst con 2FA obligatorio).
- PCI DSS Nivel 4: Escaneo de vulnerabilidades trimestral (ASV externo).
- PCI DSS Nivel 4: Log de auditoría para accesos a datos de pago, retención 1 año mínimo.
- Resolución DIAN 000042/2020 — Facturación electrónica: Factura electrónica en formato XML UBL 2.1 firmada con certificado digital DIAN.
- Resolución DIAN 000042/2020 — Facturación electrónica: Envío al proveedor tecnológico autorizado (habilitado por DIAN) antes de entregar al adquirente.
- Resolución DIAN 000042/2020 — Facturación electrónica: Nota crédito electrónica para anulaciones o correcciones.
- Resolución DIAN 000042/2020 — Facturación electrónica: Numeración autorizada con CUFE (Código Único de Facturación Electrónica).
- Resolución DIAN 000042/2020 — Facturación electrónica: Retención de XML firmados por 5 años mínimo (el sistema los retiene 7 años por política interna).
- Seguridad de datos: Ningún campo PII en logs; PAN tokenizado en tránsito y en reposo.
- Logging: MDC obligatorio por request para correlationId, tagId (enmascarado), stationId, traceId.
- Logging: PII prohibida en logs; usar toMasked() para PII antes de loggear.

## Compliance
Ley 1581/2012 — Habeas data y protección de datos personales: PII clasificada como dato sensible; almacenamiento cifrado en reposo (AES-256 vía Cloud SQL encryption); logs no contienen PII (enmascaramiento); derecho de supresión (endpoint DELETE /api/v1/accounts/{{userId}} con anonimización); aviso de privacidad y consentimiento explícito; registro de tratamiento de datos ante SIC.
PCI DSS Nivel 4: Aplica para procesamiento de números de tarjeta (recargas PayU); PAN tokenizado (nunca se almacena completo, solo token de PayU); transmisión de datos de tarjeta siempre vía HTTPS/TLS 1.3; acceso a datos de tarjeta restringido por roles (FinanceAnalyst con 2FA); escaneo de vulnerabilidades trimestral (ASV externo); log de auditoría para accesos a datos de pago, retención 1 año mínimo.
Resolución DIAN 000042/2020 — Facturación electrónica: Factura electrónica en formato XML UBL 2.1 firmada con certificado digital DIAN; envío a proveedor tecnológico autorizado antes de entregar al adquirente; nota crédito electrónica para anulaciones/correcciones; numeración autorizada con CUFE; retención de XML firmados por 5 años mínimo (sistema retiene 7 años).
SuperTransporte — Reportes regulatorios: Reportes mensuales de pasos por categoría vehicular y estación (out of scope MVP, release 2); arquitectura debe soportar adición de adaptador de reporte sin cambios en el dominio.

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
