# ADR-000 — Baseline del stack

- **Status**: Accepted
- **Date**: autogenerado — editar si cambia

## Context

Repo generado por `agentic init`. Se detectaron las decisiones del stack a partir de los archivos de build presentes y la configuración del TLM.

## Decision

- Lenguaje primario: **java**
- Framework: **spring-boot**
- Build tool: **maven**
- Test framework: **junit**
- Governance score mínimo (quality gate): **75/100**

## Consequences

- Cualquier cambio de estos baselines requiere un nuevo ADR (`docs/adr/NNN-titulo.md`).
- `agentic check` valida contra este baseline; Guardian bloquea merge si el score cae por debajo del quality gate.
