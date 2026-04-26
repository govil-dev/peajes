/**
 * Shared utilities for Continue.dev slash commands.
 * Mirrors patterns from agent-cli/src/apiClient.ts and agent-cli/src/config.ts.
 *
 * Types are inlined — @continuedev/core is provided by the Continue.dev runtime.
 * Node.js built-ins available because Continue.dev runs providers as CommonJS.
 */

import * as fs from "fs";
import * as path from "path";
import * as os from "os";
import * as child_process from "child_process";
import * as nodeCrypto from "crypto";
import { fileURLToPath } from "url";

// ─── Path normalization ───────────────────────────────────────────────────────

/**
 * Converts a workspace directory to a filesystem path.
 * Continue.dev's ide.getWorkspaceDirs() returns file:// URIs (e.g.
 * "file:///home/user/project"), not plain paths. fileURLToPath handles
 * percent-decoding and platform differences correctly.
 */
export function toFsPath(raw: string): string {
  if (raw.startsWith("file://")) {
    try {
      return fileURLToPath(raw);
    } catch {
      // Malformed URI — fall back to stripping the scheme manually
      return raw.replace(/^file:\/\//, "");
    }
  }
  return raw;
}

// ─── .env.local loader ───────────────────────────────────────────────────────
//
// Continue.dev slash commands run inside the VS Code extension host process and
// do NOT load .env.local from the project directory. This module-level cache
// is populated by loadEnvFromWorkspace() on each command invocation so that
// per-project env vars (AGENTIC_AGENT_URL, DEVCORE_STREAM_URL, etc.) are
// respected without mutating process.env globally.

const _envCache = new Map<string, string>();

/**
 * Reads .env.local from workspaceDir and populates the internal env cache.
 * Clears any previous values on each call (fresh reload per invocation).
 * Safe to call multiple times — silently skips missing or unreadable files.
 */
export function loadEnvFromWorkspace(workspaceDir: string): void {
  _envCache.clear();
  try {
    const content = fs.readFileSync(
      path.join(workspaceDir, ".env.local"),
      "utf-8"
    );
    for (const line of content.split("\n")) {
      const trimmed = line.trim();
      if (!trimmed || trimmed.startsWith("#")) continue;
      const eq = trimmed.indexOf("=");
      if (eq === -1) continue;
      const key = trimmed.slice(0, eq).trim();
      const value = trimmed.slice(eq + 1).trim();
      if (key) _envCache.set(key, value);
    }
  } catch {
    // .env.local absent or unreadable — fall through to process.env defaults
  }
}

/** Reads a variable from the workspace .env.local cache first, then process.env. */
function getEnv(key: string): string | undefined {
  return _envCache.get(key) ?? process.env[key];
}

// ─── Endpoint URLs (mirror env vars from agent-cli) ──────────────────────────

const GUARDIAN_URL_DEFAULT =
  "https://langchain-guardian-devcore-pihnhikstq-uc.a.run.app/agent/manual_chat";
const DEVCORE_STREAM_URL_DEFAULT = "https://code-generator-pihnhikstq-uc.a.run.app/agent/stream";
const DEVCORE_URL_DEFAULT = "https://code-generator-pihnhikstq-uc.a.run.app/agent/create-code";

export function getGuardianUrl(): string {
  return getEnv("AGENTIC_AGENT_URL") ?? GUARDIAN_URL_DEFAULT;
}

export function getDevCoreStreamUrl(): string {
  return getEnv("DEVCORE_STREAM_URL") ?? DEVCORE_STREAM_URL_DEFAULT;
}

export function getDevCoreUrl(): string {
  return getEnv("DEVCORE_AGENT_URL") ?? DEVCORE_URL_DEFAULT;
}

/**
 * Returns the base URL for the Agentic backend (TLM).
 * Derives origin from AGENTIC_PROJECTS_URL if present in .env.local cache,
 * otherwise defaults to http://localhost:8080.
 */
export function getApiBaseUrl(): string {
  const projectsUrl = getEnv("AGENTIC_PROJECTS_URL");
  if (projectsUrl) {
    try {
      return new URL(projectsUrl).origin;
    } catch {
      // malformed URL — fall through
    }
  }
  return "http://localhost:8080";
}

// ─── Auth ─────────────────────────────────────────────────────────────────────

export function readToken(): string | null {
  try {
    const tokenPath = path.join(os.homedir(), ".agentic", "token");
    const value = fs.readFileSync(tokenPath, "utf-8").trim();
    return value.length > 0 ? value : null;
  } catch {
    return null;
  }
}

/** Headers for DevCore Agent (JWT Bearer). */
export function makeBearerHeaders(): Record<string, string> {
  const headers: Record<string, string> = { "Content-Type": "application/json" };
  const token = readToken();
  if (token) headers["Authorization"] = `Bearer ${token}`;
  return headers;
}

/** Headers for Guardian Agent (X-API-Key). */
export function makeGuardianHeaders(): Record<string, string> {
  const headers: Record<string, string> = { "Content-Type": "application/json" };
  const apiKey = getEnv("GUARDIAN_API_KEY");
  if (apiKey) headers["X-API-Key"] = apiKey;
  return headers;
}

// ─── Git helpers (spawnSync with args array — no injection risk) ──────────────

export function getGitRemote(cwd: string): string | null {
  const result = child_process.spawnSync("git", ["remote", "get-url", "origin"], {
    cwd,
    encoding: "utf-8",
  });
  if (result.status !== 0) return null;
  return result.stdout.trim() || null;
}

export function getGitBranch(cwd: string): string {
  const result = child_process.spawnSync("git", ["branch", "--show-current"], {
    cwd,
    encoding: "utf-8",
  });
  if (result.status !== 0) return "main";
  return result.stdout.trim() || "main";
}

// ─── Agentic config files ─────────────────────────────────────────────────────

export interface AgenticConfig {
  project_code: string;
  project_name: string;
  language: string;
  project_id?: string;
}

export function readAgenticConfig(workspaceDir: string): AgenticConfig | null {
  try {
    const raw = fs.readFileSync(path.join(workspaceDir, ".agentic.json"), "utf-8");
    return JSON.parse(raw) as AgenticConfig;
  } catch {
    return null;
  }
}

export interface AuthorProfile {
  author_name: string;
  author_email: string;
}

export function readAuthorProfile(): AuthorProfile | null {
  try {
    const raw = fs.readFileSync(path.join(os.homedir(), ".agentic", "profile.json"), "utf-8");
    return JSON.parse(raw) as AuthorProfile;
  } catch {
    return null;
  }
}

// ─── Misc ─────────────────────────────────────────────────────────────────────

export function newThreadId(): string {
  return nodeCrypto.randomUUID();
}

export function toSnakeCase(str: string): string {
  return str.trim().toLowerCase().replace(/[\s-]+/g, "_");
}

// ─── Pending checkpoint state ─────────────────────────────────────────────────
//
// Persists between command invocations within the same VS Code session
// (Node.js module cache is shared across slash command calls).

export interface PendingCheckpoint {
  threadId: string;
  currentNode: string;
  summary: string;
  files: string[];
  workspaceDir: string;
}

let _pendingCheckpoint: PendingCheckpoint | null = null;

export function setPendingCheckpoint(cp: PendingCheckpoint): void {
  _pendingCheckpoint = cp;
}

export function getPendingCheckpoint(): PendingCheckpoint | null {
  return _pendingCheckpoint;
}

export function clearPendingCheckpoint(): void {
  _pendingCheckpoint = null;
}

// ─── Guardian response types ──────────────────────────────────────────────────

export interface GuardianIssue {
  type: string;
  severity: string;
  description: string;
  suggestion: string;
}

export interface GuardianResponse {
  governance_score: number;
  status: string;
  stats: { total_issues: number; files_affected: number };
  results_by_file: Record<string, GuardianIssue[]>;
}

export function isGuardianResponse(value: unknown): value is GuardianResponse {
  return (
    typeof value === "object" &&
    value !== null &&
    typeof (value as Record<string, unknown>)["governance_score"] === "number" &&
    typeof (value as Record<string, unknown>)["status"] === "string"
  );
}

// ─── Guardian result formatter ────────────────────────────────────────────────

export function formatGuardianResult(result: GuardianResponse): string {
  const score = result.governance_score;
  const scoreEmoji = score >= 70 ? "✅" : score >= 40 ? "⚠️" : "❌";
  const lines: string[] = [];

  lines.push(`## ${scoreEmoji} Governance Score: ${score}/100`);
  lines.push(`**Status**: ${result.status}`);
  lines.push(`**Issues**: ${result.stats.total_issues} in ${result.stats.files_affected} file(s)`);

  const allIssues = Object.entries(result.results_by_file).flatMap(([file, issues]) =>
    issues.map((issue) => ({ file, ...issue }))
  );

  if (allIssues.length === 0) {
    lines.push("\n✅ No violations found.");
    return lines.join("\n");
  }

  lines.push("\n### Violations\n");
  for (const issue of allIssues) {
    const sevEmoji =
      issue.severity === "Critical" || issue.severity === "High" ? "🔴" : "🟡";
    lines.push(`${sevEmoji} **${issue.file}** — ${issue.type} (${issue.severity})`);
    lines.push(`   ${issue.description}`);
    lines.push(`   💡 ${issue.suggestion}`);
    lines.push("");
  }

  return lines.join("\n");
}
