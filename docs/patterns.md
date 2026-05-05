# Design patterns

> Generado por Guardian Suite (FACTORY-30). Fuente: TLM.

Value Object: Explicitly defined for immutable data types like `GovernanceScore`, `Severity`, and `CommitSha`, with validation performed in their constructors (`__post_init__`). [category=general]
Retry Pattern: Applied to external service calls (e.g., DIAN proveedor tecnológico) to handle transient failures by re-attempting operations. [category=general]
Fallback Pattern: Used in external service integrations (e.g., ANI API) to provide alternative behavior or cached data when the primary service is unavailable. [category=general]
Idempotent Consumer: Implemented for event consumers (e.g., TollPassRegistered) to ensure events are processed only once, even if received multiple times, using unique identifiers like passId for deduplication. [category=general]
