/**
 * Custom context provider for Continue.dev.
 * Loads use cases from the active project via the Agentic backend.
 * Reads .agentic.json from workspace to find project_id, then fetches
 * GET /api/v1/projects/{id} and extracts domain_definition.use_case[].
 *
 * Types are inlined because @continuedev/core is provided by the runtime.
 */

import * as fs from "fs";
import * as os from "os";
import * as path from "path";
import { fileURLToPath } from "url";

// ─── Continue.dev types (provided by runtime) ─────────────────────────────────

interface ContextItem {
  name: string;
  description: string;
  content: string;
}

interface IdeInterface {
  getWorkspaceDirs(): Promise<string[]>;
  readFile(fileUri: string): Promise<string>;
  fileExists(fileUri: string): Promise<boolean>;
}

interface ContextProviderExtras {
  config?: { params?: Record<string, unknown> };
  ide?: IdeInterface;
}

interface CustomContextProvider {
  name: string;
  description?: string;
  getContextItems(query: string, extras: ContextProviderExtras): Promise<ContextItem[]>;
}

// ─── Token reader ─────────────────────────────────────────────────────────────

function readToken(): string | null {
  try {
    const value = fs.readFileSync(path.join(os.homedir(), ".agentic", "token"), "utf-8").trim();
    return value.length > 0 ? value : null;
  } catch {
    return null;
  }
}

// ─── .agentic.json reader via IDE ─────────────────────────────────────────────

interface AgenticConfig {
  project_id?: string;
  project_code?: string;
  project_name?: string;
}

async function readAgenticConfigFromIde(ide: IdeInterface): Promise<AgenticConfig | null> {
  try {
    const dirs = await ide.getWorkspaceDirs();
    if (!dirs.length) return null;
    const rawDir = dirs[0];
    const workspaceDir = rawDir.startsWith("file://") ? fileURLToPath(rawDir) : rawDir;
    const configPath = `${workspaceDir}/.agentic.json`;
    if (!(await ide.fileExists(configPath))) return null;
    return JSON.parse(await ide.readFile(configPath)) as AgenticConfig;
  } catch {
    return null;
  }
}

// ─── Use case types ───────────────────────────────────────────────────────────

interface UseCase {
  id: string;
  code: string;
  title: string;
  complexity?: string;
  description?: string;
}

interface ProjectDetail {
  name: string;
  code: string;
  domain_definition?: { use_case?: UseCase[] } | null;
}

interface ProjectDetailResponse {
  body?: ProjectDetail | null;
}

// ─── Fetch use cases ──────────────────────────────────────────────────────────

async function fetchUseCases(
  baseUrl: string,
  projectId: string,
  token: string | null
): Promise<UseCase[]> {
  const headers: Record<string, string> = { "Content-Type": "application/json" };
  if (token) headers["Authorization"] = `Bearer ${token}`;
  const res = await fetch(
    `${baseUrl}/api/v1/projects/${encodeURIComponent(projectId)}`,
    { headers, signal: AbortSignal.timeout(8_000) }
  );
  if (!res.ok) return [];
  const data = (await res.json()) as ProjectDetailResponse;
  return data.body?.domain_definition?.use_case ?? [];
}

// ─── Context provider ─────────────────────────────────────────────────────────

const AgenticUseCasesProvider: CustomContextProvider = {
  name: "agentic-usecases",
  description: "Use cases of the active project from Agentic backend",

  async getContextItems(
    _query: string,
    extras: ContextProviderExtras
  ): Promise<ContextItem[]> {
    const apiUrl =
      (extras.config?.params?.["apiUrl"] as string | undefined) ?? "http://localhost:8080";
    const baseUrl = apiUrl.replace(/\/$/, "");
    const token = readToken();

    if (!extras.ide) return [];

    const agenticConfig = await readAgenticConfigFromIde(extras.ide);
    const projectId = agenticConfig?.project_id;
    if (!projectId) return [];

    let useCases: UseCase[];
    try {
      useCases = await fetchUseCases(baseUrl, projectId, token);
    } catch {
      return [];
    }

    if (!useCases.length) return [];

    const header = `# Casos de uso — ${agenticConfig?.project_code ?? projectId}`;
    const lines = useCases.map((uc) => {
      const complexity = uc.complexity ? ` _(${uc.complexity})_` : "";
      const desc = uc.description ? `\n  ${uc.description}` : "";
      return `- **${uc.code}** — ${uc.title}${complexity}${desc}`;
    });

    return [
      {
        name: `Casos de uso (${useCases.length})`,
        description: agenticConfig?.project_code ?? projectId,
        content: [header, ...lines].join("\n"),
      },
    ];
  },
};

export default AgenticUseCasesProvider;
