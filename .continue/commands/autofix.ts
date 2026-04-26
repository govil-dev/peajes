/**
 * /autofix slash command for Continue.dev.
 *
 * Reads the last Guardian result from .agentic-guardian-result.json (written
 * by /validate and /review), sends violations to DevCore Agent /agent/autofix
 * (SSE), and streams corrected file paths into the chat.
 *
 * Usage: /autofix
 */

import * as fs from "fs";
import * as path from "path";
import {
  getDevCoreStreamUrl,
  makeBearerHeaders,
  readAgenticConfig,
  loadEnvFromWorkspace,
} from "./shared";

// ─── Types matching DevCore AutofixRequest ────────────────────────────────────

interface ViolationItem {
  file: string;
  type: string;
  severity: string;
  description: string;
  suggestion: string;
}

interface GuardianResult {
  governance_score?: number;
  status?: string;
  results_by_file?: Record<string, ViolationItem[]>;
}

// ─── Derive autofix URL from stream URL ───────────────────────────────────────

function getAutofixUrl(): string {
  const streamUrl = getDevCoreStreamUrl();
  try {
    return `${new URL(streamUrl).origin}/agent/autofix`;
  } catch {
    return "https://devcore.duckdns.org/agent/autofix";
  }
}

// ─── Read last guardian result ────────────────────────────────────────────────

function readLastGuardianResult(workspaceDir: string): GuardianResult | null {
  try {
    const raw = fs.readFileSync(
      path.join(workspaceDir, ".agentic-guardian-result.json"),
      "utf-8"
    );
    return JSON.parse(raw) as GuardianResult;
  } catch {
    return null;
  }
}

// ─── Main generator ───────────────────────────────────────────────────────────

const MAX_SSE_BUFFER = 10 * 1024 * 1024;

export async function* runAutofix(workspaceDir: string): AsyncGenerator<string> {
  loadEnvFromWorkspace(workspaceDir);

  const config = readAgenticConfig(workspaceDir);
  if (!config) {
    yield "❌ `.agentic.json` not found. Run `agentic init` in your project first.";
    return;
  }

  const lastResult = readLastGuardianResult(workspaceDir);
  if (!lastResult) {
    yield "❌ No Guardian result found. Run `/validate` first to detect violations.";
    return;
  }

  const violations: ViolationItem[] = Object.entries(
    lastResult.results_by_file ?? {}
  ).flatMap(([file, issues]) =>
    issues.map((issue) => ({
      file,
      type: issue.type,
      severity: issue.severity,
      description: issue.description,
      suggestion: issue.suggestion,
    }))
  );

  if (!violations.length) {
    yield "✅ No violations to fix. Your code is clean!";
    return;
  }

  yield [
    `🔧 **Autofix** — ${violations.length} violation(s) in ${config.project_code}`,
    `- **Working dir**: \`${workspaceDir}\``,
    `- Sending to DevCore Agent...`,
  ].join("\n");

  const autofixUrl = getAutofixUrl();

  let res: Response;
  try {
    res = await fetch(autofixUrl, {
      method: "POST",
      headers: makeBearerHeaders(),
      body: JSON.stringify({
        working_directory: workspaceDir,
        project_code: config.project_code,
        violations,
      }),
      signal: AbortSignal.timeout(10 * 60 * 1000),
    });
  } catch (err) {
    yield `❌ DevCore Agent not available at \`${autofixUrl}\`.\n\n${err instanceof Error ? err.message : String(err)}`;
    return;
  }

  if (!res.ok) {
    yield `❌ Autofix error: ${res.status} ${res.statusText}`;
    return;
  }

  if (!res.body) {
    yield "❌ Empty response from DevCore Agent.";
    return;
  }

  const reader = res.body.getReader();
  const decoder = new TextDecoder();
  let buffer = "";
  let eventType = "message";
  const fixedFiles: string[] = [];

  try {
    while (true) {
      const { done, value } = await reader.read();
      if (done) break;

      buffer += decoder.decode(value, { stream: true });

      if (buffer.length > MAX_SSE_BUFFER) {
        reader.cancel();
        yield "❌ SSE buffer exceeded limit.";
        return;
      }

      const lines = buffer.split("\n");
      buffer = lines.pop() ?? "";

      for (const line of lines) {
        if (line.startsWith("event:")) {
          eventType = line.slice("event:".length).trim();
        } else if (line.startsWith("data:")) {
          const rawData = line.slice("data:".length).trim();
          const currentEvent = eventType;
          eventType = "message";

          if (currentEvent === "error") {
            let msg = rawData;
            try { msg = (JSON.parse(rawData) as { message?: string }).message ?? rawData; } catch { /* ignore */ }
            yield `❌ Agent error: ${msg}`;
            return;
          }

          if (currentEvent === "status") {
            let parsed: { message?: string } | null = null;
            try { parsed = JSON.parse(rawData) as { message?: string }; } catch { /* ignore */ }
            yield `⟳ ${parsed?.message ?? rawData}`;
            continue;
          }

          if (currentEvent === "file_ready") {
            // autofix file_ready uses "file_path" (not "path")
            let filePath: string | undefined;
            try { filePath = (JSON.parse(rawData) as { file_path?: string }).file_path; } catch { /* ignore */ }
            // LLM02: rechazar paths con traversal o rutas absolutas antes de mostrar/usar
            if (filePath && !filePath.includes("..") && !filePath.startsWith("/")) {
              fixedFiles.push(filePath);
              yield `✏️ Fixed: \`${filePath}\``;
            }
            continue;
          }

          if (currentEvent === "completed") {
            let parsed: { message?: string; status?: string } | null = null;
            try { parsed = JSON.parse(rawData) as { message?: string; status?: string }; } catch { /* ignore */ }

            const summary = [
              parsed?.status === "error"
                ? `⚠️ Autofix finished with errors. ${parsed?.message ?? ""}`
                : `✅ **Autofix completed!** ${parsed?.message ?? ""}`,
            ];
            if (fixedFiles.length) {
              summary.push(`\n${fixedFiles.length} file(s) corrected:`);
              fixedFiles.forEach((f) => summary.push(`- \`${f}\``));
            }
            summary.push("\n> Run `/validate` to verify the fixes.");
            yield summary.join("\n");
          }
        }
      }
    }
  } finally {
    reader.cancel();
  }
}
