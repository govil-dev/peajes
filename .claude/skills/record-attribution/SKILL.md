---
name: record-attribution
description: Skill para invocar tlm_record_ai_attributions tras cada Edit/Write significativo.
---

# record-attribution (STUB)

> TODO: reemplazar por el template real cuando el track CLI publique
> `agent-cli/templates/skills/record-attribution/SKILL.md`.

## Objetivo
Después de cada Edit/Write significativo a código fuente, llamá la MCP tool
`tlm_record_ai_attributions` (servidor TLM) con file_path, line range,
generator y model. Es como el proyecto mide AI Contribution Rate y
governance.

## Payload mínimo
- `project_code` — viene en `.agentic.json`
- `commit_sha` — `null` mientras editás (TLM reconcilia al push)
- `attributions` — lista `[{file_path, line_start, line_end, generator, model}]`

Skip archivos no-código (`.md`, `.json`, lock files, imágenes).

Ver `docs/attribution.md` para el schema completo y el ejemplo JSON.
