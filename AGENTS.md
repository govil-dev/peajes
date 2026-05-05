# AGENTS.md

> Compact agent context. Detalles completos en `docs/`. Generado por Guardian Suite (FACTORY-30).

**Stack**: Spring Boot 3.3.x + Spring WebFlux / Java / Java 21 / Maven 3.9.x / JUnit 5.10

<architecture-constraints>
## Dominio
The business domain is 'Peajes Colombia' (Toll Collection in Colombia), focusing on electronic toll systems. The primary goal is to process high volumes of vehicle transactions (up to 1,500…

_Detalle: [`docs/architecture.md`](docs/architecture.md)._
</architecture-constraints>

<framework-rules>
## Working contract
1. **Empezar**: invocá la MCP tool `tlm_start_use_case(project_code, use_case_id)` para marcar el UC como `in_progress`.
2. **Escribir**: respetá `docs/standards.md`, `docs/patterns.md` y `docs/constraints.md` (cuando exista). Tras cada Edit/Write llamá `tlm_record_ai_attributions` (ver sección AI Attribution).
3. **Cerrar**:
   a) Corré `agentic check --json` y leé el campo `.governance_score` del output.
   b) Llamá `tlm_finish_use_case(..., governance_score=<score>)`.
   c) Si TLM retorna `SCORE_BELOW_THRESHOLD`: corregí violations y volvé a (a).

## Estándares clave (top-5)
- External Service Integration Resilience: Interactions with external services must implement resilien…
- Data Validation & Error Handling: String fields must adhere to specified length limits. Numeric fiel…
- Financial Data Type Representation: Monetary amounts and balances must be represented as strings wit…
- Kafka Event Delivery & Idempotency: Kafka producers must ensure 'at-least-once' delivery for critica…
- Event Delivery Semantics: Kafka producers must ensure 'at-least-once' delivery for critical events,…

_Listado completo: [`docs/standards.md`](docs/standards.md)._

## Patrones clave (top-5)
- Value Object: Explicitly defined for immutable data types like `GovernanceScore`, `Severity`, and `C…
- Retry Pattern: Applied to external service calls (e.g., DIAN proveedor tecnológico) to handle transi…
- Fallback Pattern: Used in external service integrations (e.g., ANI API) to provide alternative behav…
- Idempotent Consumer: Implemented for event consumers (e.g., TollPassRegistered) to ensure events are…

_Listado completo: [`docs/patterns.md`](docs/patterns.md)._
</framework-rules>

<quality-gate>
## Quality gate
Governance score mínimo **60/100**. Validar con `agentic check`.
</quality-gate>

## AI Attribution via MCP

Después de cada Edit/Write significativo a código, llamá la MCP tool `tlm_record_ai_attributions` con `project_code="PEAJES-CO-01"`. Header: `X-API-Key` (obtené con `agentic mcp-key --print`).

```json
{
  "project_code": "PEAJES-CO-01",
  "commit_sha": null,
  "attributions": [
    {
      "file_path": "src/foo.py", "line_start": 12, "line_end": 14,
      "generator": "claude-code", "model": "claude-opus-4-7"
    }
  ]
}
```

Schema completo y notas en [`docs/attribution.md`](docs/attribution.md).

## Pointers
Documentos referenciados:
- [`docs/standards.md`](docs/standards.md)
- [`docs/patterns.md`](docs/patterns.md)
- [`docs/architecture.md`](docs/architecture.md)
- [`docs/structure.md`](docs/structure.md)
- [`docs/nfr.md`](docs/nfr.md)
- [`docs/constraints.md`](docs/constraints.md)
- [`docs/compliance.md`](docs/compliance.md)
- [`docs/environments.md`](docs/environments.md)
