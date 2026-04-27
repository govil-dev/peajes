/**
 * /validate slash command for Continue.dev.
 *
 * POSTs to Guardian Agent /agent/manual_chat with the full project context
 * (repo URL, branch, project code, author, and up to 50 local files).
 * Renders score + violations in the chat.
 */

import * as fs from "fs";
import * as path from "path";
import {
  getGuardianUrl,
  makeGuardianHeaders,
  getGitRemote,
  getGitBranch,
  readAgenticConfig,
  readAuthorProfile,
  newThreadId,
  isGuardianResponse,
  formatGuardianResult,
} from "./shared";

// ─── Local file collection (mirrors agent-cli check.ts) ──────────────────────

const CODE_EXTENSIONS = new Set([
  ".java", ".go", ".ts", ".py", ".kt",
  ".xml", ".yaml", ".yml", ".json",
]);
const EXCLUDED_DIRS = new Set([
  "node_modules", ".git", "target", "build", "dist",
  "__pycache__", ".next", ".venv", "venv",
]);
const MAX_FILES = 50;
const MAX_FILE_BYTES = 50 * 1024;

function collectFiles(
  dir: string,
  relBase: string,
  results: Array<{ path: string; content: string }>
): void {
  if (results.length >= MAX_FILES) return;
  let entries: fs.Dirent[];
  try {
    entries = fs.readdirSync(dir, { withFileTypes: true });
  } catch {
    return;
  }
  for (const entry of entries) {
    if (results.length >= MAX_FILES) break;
    if (EXCLUDED_DIRS.has(entry.name)) continue;
    const full = path.join(dir, entry.name);
    const rel = relBase ? `${relBase}/${entry.name}` : entry.name;
    if (entry.isDirectory()) {
      collectFiles(full, rel, results);
    } else if (
      entry.isFile() &&
      CODE_EXTENSIONS.has(path.extname(entry.name).toLowerCase())
    ) {
      try {
        const stat = fs.statSync(full);
        if (stat.size > MAX_FILE_BYTES) continue;
        results.push({ path: rel, content: fs.readFileSync(full, "utf-8") });
      } catch {
        // skip unreadable files
      }
    }
  }
}

// ─── Main generator ───────────────────────────────────────────────────────────

export async function* runValidate(workspaceDir: string): AsyncGenerator<string> {
  const config = readAgenticConfig(workspaceDir);
  if (!config) {
    yield "❌ `.agentic.json` not found. Run `agentic init` in your project first.";
    return;
  }

  const profile = readAuthorProfile();
  if (!profile) {
    yield "❌ Author profile not found. Run `agentic login` in the terminal first.";
    return;
  }

  const repo_url = getGitRemote(workspaceDir);
  if (!repo_url) {
    yield "❌ No git remote `origin` detected in this workspace.";
    return;
  }

  const branch = getGitBranch(workspaceDir);

  yield `🔍 Analyzing **${config.project_code}** (branch: \`${branch}\`)...\n\nCollecting files...`;

  const files_content: Array<{ path: string; content: string }> = [];
  collectFiles(workspaceDir, "", files_content);

  yield `📦 Sending ${files_content.length} file(s) to Guardian Agent...`;

  const guardianUrl = getGuardianUrl();

  let res: Response;
  try {
    res = await fetch(guardianUrl, {
      method: "POST",
      headers: makeGuardianHeaders(),
      body: JSON.stringify({
        input: {
          repo_url,
          target_language: config.language,
          target_branch: branch,
          author_name: profile.author_name,
          author_email: profile.author_email,
          project_code: config.project_code,
          ...(files_content.length > 0 ? { files_content } : {}),
        },
        config: { configurable: { thread_id: newThreadId() } },
      }),
      signal: AbortSignal.timeout(10 * 60 * 1000),
    });
  } catch (err) {
    yield `❌ Guardian Agent is not available at \`${guardianUrl}\`.\n\nMake sure it's running.\n\n${
      err instanceof Error ? err.message : String(err)
    }`;
    return;
  }

  if (!res.ok) {
    yield `❌ Guardian error: ${res.status} ${res.statusText}`;
    return;
  }

  let data: unknown;
  try {
    data = await res.json();
  } catch {
    yield "❌ Guardian returned an invalid JSON response.";
    return;
  }

  const body = (data as Record<string, unknown>)["body"] ?? data;

  if (!isGuardianResponse(body)) {
    yield "❌ Unexpected Guardian response format (missing `governance_score` or `status`).";
    return;
  }

  // Write result file so the VS Code extension can update diagnostics automatically.
  // The extension watches .agentic-guardian-result.json via FileSystemWatcher.
  try {
    fs.writeFileSync(
      path.join(workspaceDir, ".agentic-guardian-result.json"),
      JSON.stringify(body, null, 2),
      "utf-8",
    );
  } catch {
    // non-fatal — diagnostics update is best-effort
  }

  yield formatGuardianResult(body);
}
