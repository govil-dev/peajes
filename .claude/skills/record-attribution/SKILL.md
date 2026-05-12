---
name: record-attribution
description: Register AI-generated line ranges with tlm_record_ai_attributions after significant code edits.
version: 1.0.0
triggers:
  - Before finish_use_case
  - After a significant batch of Edit or Write operations on source code files
---

## When to call

Call `tlm_record_ai_attributions` from the TechLeadManager MCP server after each significant Edit or Write to source code files. This is how the project tracks AI Contribution Rate, retention, and governance metrics.

Source code extensions: `.py`, `.ts`, `.tsx`, `.js`, `.jsx`, `.java`, `.kt`, `.go`, `.rb`, `.rs`, `.cs`.

Do not call for non-code files (`.md`, `.json`, `.yml`, build configs, images, lock files).

## Payload fields

- `project_code` (required): value in `.agentic.json` — already set for this workspace
- `commit_sha` (optional): leave `null` while editing — TLM reconciles to the real SHA when you push the PR
- `attributions` (required, array): one item per range you generated:
  - `file_path`: relative to the repo root
  - `line_start`, `line_end`: 1-indexed, inclusive
  - `generator`: e.g. `"claude-code"`, `"cursor"`, `"copilot"`
  - `model`: e.g. `"claude-opus-4-7"`, `"gpt-4o"`
  - `agent_node`: (optional) for LangGraph flows

To obtain the `X-API-Key` for the TLM MCP server, run `agentic mcp-key --print` (persisted in `~/.agentic/profile.json` after `agentic login`).

## Anti-patterns

- Do not record typo-only or whitespace-only changes
- Do not invent line numbers — use only the exact lines you generated
- Do not record edits to non-source files (.md, .json, .yml, build configs, lock files)
- Do not batch human-written lines with AI-generated lines in the same attribution item

<example>
<description>Single edit: 3 lines generated in a Java file before finish_use_case</description>
<tool_call>
{
  "server": "tech-lead-manager",
  "tool": "tlm_record_ai_attributions",
  "arguments": {
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
}
</tool_call>
</example>
