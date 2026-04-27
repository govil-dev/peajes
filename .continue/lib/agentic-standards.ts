/**
 * Custom context provider for Continue.dev.
 * Loads coding standards and design patterns from the Agentic backend.
 * When extras.ide is available and .agentic.json has a project_id, fetches
 * the 5 project-scoped context fields from /api/v1/projects/{id}.
 * Falls back to the global org catalog, then to .continue/context/standards.json.
 *
 * Types are inlined because @continuedev/core is provided by the
 * Continue.dev runtime — no package.json dependency needed.
 */

import * as fs from "fs";
import * as path from "path";
import * as os from "os";
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
  config?: {
    params?: Record<string, unknown>;
  };
  ide?: IdeInterface;
}

interface CustomContextProvider {
  name: string;
  description?: string;
  getContextItems(query: string, extras: ContextProviderExtras): Promise<ContextItem[]>;
}

// ─── Static fallback ──────────────────────────────────────────────────────────

const FALLBACK_PATH = path.join(__dirname, "..", "standards.json");

interface StaticStandard {
  id: string;
  title: string;
  description: string;
  category: string;
  language: string;
}

function loadFallback(): ContextItem[] {
  try {
    const raw = fs.readFileSync(FALLBACK_PATH, "utf-8");
    const items: StaticStandard[] = JSON.parse(raw) as StaticStandard[];
    return items.map((s) => ({
      name: s.title,
      description: `[${s.category}] ${s.language}`,
      content: `## ${s.title}\n${s.description}`,
    }));
  } catch {
    return [];
  }
}

// ─── Token reader ─────────────────────────────────────────────────────────────

function readToken(): string | null {
  try {
    const tokenPath = path.join(os.homedir(), ".agentic", "token");
    const value = fs.readFileSync(tokenPath, "utf-8").trim();
    return value.length > 0 ? value : null;
  } catch {
    return null;
  }
}

// ─── Global catalog API helpers ───────────────────────────────────────────────

interface ApiItem {
  id?: string;
  name?: string;
  title?: string;
  description?: string;
  rule?: string;
}

interface ApiResponseBody {
  items?: ApiItem[];
}

interface ApiResponse {
  body?: ApiResponseBody | ApiItem[] | null;
  error_message?: string | null;
}

function extractItems(json: ApiResponse): ApiItem[] {
  const body = json?.body;
  if (body == null) return [];
  if (Array.isArray(body)) return body;
  if (Array.isArray(body.items)) return body.items;
  return [];
}

function itemToContextItem(prefix: string, item: ApiItem): ContextItem {
  const name = item.title ?? item.name ?? item.id ?? "Standard";
  const description = item.description ?? item.rule ?? "";
  return {
    name: `[${prefix}] ${name}`,
    description: prefix,
    content: `## ${name}\n${description}`,
  };
}

async function fetchJson(url: string, token: string | null): Promise<ApiResponse> {
  const headers: Record<string, string> = { "Content-Type": "application/json" };
  if (token) headers["Authorization"] = `Bearer ${token}`;
  const res = await fetch(url, { headers, signal: AbortSignal.timeout(8_000) });
  if (!res.ok) throw new Error(`HTTP ${res.status} from ${url}`);
  const text = await res.text();
  if (!text) return { body: null };
  return JSON.parse(text) as ApiResponse;
}

// ─── Project-scoped types ─────────────────────────────────────────────────────

interface ProjectCodingStandard {
  name: string;
  description?: string;
  category?: string;
  language?: string;
}

interface ProjectDesignPattern {
  name: string;
  description?: string;
  implementation_guide?: string;
}

interface ProjectStructureDefinition {
  name: string;
  content?: string;
  package_base_example?: string;
  main_class_pattern?: string;
}

interface ProjectInfrastructureDefinition {
  name: string;
  db_engines?: string[];
  api_types?: string[];
  messaging?: string[];
  content?: string;
}

interface ProjectUseCase {
  code: string;
  title: string;
  complexity?: string;
  description?: string;
}

interface ProjectDomainDefinition {
  name: string;
  description?: string;
  use_case?: ProjectUseCase[];
}

interface ProjectDetail {
  name: string;
  code: string;
  coding_standards?: ProjectCodingStandard[];
  design_patterns?: ProjectDesignPattern[];
  structure_definition?: ProjectStructureDefinition | null;
  infrastructure_definition?: ProjectInfrastructureDefinition | null;
  domain_definition?: ProjectDomainDefinition | null;
}

interface ProjectDetailResponse {
  body?: ProjectDetail | null;
}

// ─── .agentic.json reader via IDE interface ───────────────────────────────────

interface AgenticConfig {
  project_id?: string;
  project_code?: string;
  project_name?: string;
}

async function readAgenticConfigFromIde(
  ide: IdeInterface
): Promise<AgenticConfig | null> {
  try {
    const dirs = await ide.getWorkspaceDirs();
    if (!dirs.length) return null;
    const rawDir = dirs[0];
    const workspaceDir = rawDir.startsWith("file://") ? fileURLToPath(rawDir) : rawDir;
    const configPath = `${workspaceDir}/.agentic.json`;
    const exists = await ide.fileExists(configPath);
    if (!exists) return null;
    const raw = await ide.readFile(configPath);
    return JSON.parse(raw) as AgenticConfig;
  } catch {
    return null;
  }
}

// ─── Project detail fetcher ───────────────────────────────────────────────────

async function fetchProjectDetail(
  baseUrl: string,
  projectId: string,
  token: string | null
): Promise<ProjectDetail | null> {
  try {
    const url = `${baseUrl}/api/v1/projects/${encodeURIComponent(projectId)}`;
    const headers: Record<string, string> = { "Content-Type": "application/json" };
    if (token) headers["Authorization"] = `Bearer ${token}`;
    const res = await fetch(url, { headers, signal: AbortSignal.timeout(8_000) });
    if (!res.ok) return null;
    const data = (await res.json()) as ProjectDetailResponse;
    return data.body ?? null;
  } catch {
    return null;
  }
}

// ─── Project context item builder ─────────────────────────────────────────────

function buildProjectContextItems(project: ProjectDetail): ContextItem[] {
  const items: ContextItem[] = [];
  const header = `# Proyecto: ${project.name} (${project.code})`;

  if (project.coding_standards?.length) {
    const content = [
      header,
      "## Estándares de código",
      ...project.coding_standards.map(
        (s) =>
          `### ${s.name}\n${s.description ?? ""}${s.language ? `\n_Lenguaje: ${s.language}_` : ""}`
      ),
    ].join("\n\n");
    items.push({ name: "Estándares de código", description: project.code, content });
  }

  if (project.design_patterns?.length) {
    const content = [
      header,
      "## Patrones de diseño",
      ...project.design_patterns.map(
        (p) =>
          `### ${p.name}\n${p.description ?? ""}${p.implementation_guide ? `\n\n**Guía:** ${p.implementation_guide}` : ""}`
      ),
    ].join("\n\n");
    items.push({ name: "Patrones de diseño", description: project.code, content });
  }

  if (project.structure_definition) {
    const s = project.structure_definition;
    const parts = [header, `## Estructura del proyecto\n**${s.name}**`];
    if (s.content) parts.push(s.content);
    if (s.package_base_example)
      parts.push(`**Base de paquetes:** \`${s.package_base_example}\``);
    if (s.main_class_pattern)
      parts.push(`**Patrón de clase principal:** \`${s.main_class_pattern}\``);
    items.push({
      name: "Estructura del proyecto",
      description: project.code,
      content: parts.join("\n\n"),
    });
  }

  if (project.infrastructure_definition) {
    const i = project.infrastructure_definition;
    const parts = [header, `## Infraestructura\n**${i.name}**`];
    if (i.db_engines?.length) parts.push(`**Bases de datos:** ${i.db_engines.join(", ")}`);
    if (i.api_types?.length) parts.push(`**Tipos de API:** ${i.api_types.join(", ")}`);
    if (i.messaging?.length) parts.push(`**Messaging:** ${i.messaging.join(", ")}`);
    if (i.content) parts.push(i.content);
    items.push({
      name: "Infraestructura",
      description: project.code,
      content: parts.join("\n\n"),
    });
  }

  if (project.domain_definition) {
    const d = project.domain_definition;
    const parts = [header, `## Dominio de negocio\n**${d.name}**`];
    if (d.description) parts.push(d.description);
    if (d.use_case?.length) {
      parts.push("### Casos de uso activos");
      d.use_case.forEach((uc) => {
        parts.push(
          `- **${uc.code}** — ${uc.title}${uc.complexity ? ` _(${uc.complexity})_` : ""}${uc.description ? `\n  ${uc.description}` : ""}`
        );
      });
    }
    items.push({
      name: "Dominio de negocio",
      description: project.code,
      content: parts.join("\n\n"),
    });
  }

  return items;
}

// ─── Context provider ─────────────────────────────────────────────────────────

const AgenticStandardsProvider: CustomContextProvider = {
  name: "agentic-standards",
  description: "Coding standards and design patterns from Agentic backend",

  async getContextItems(
    _query: string,
    extras: ContextProviderExtras
  ): Promise<ContextItem[]> {
    const apiUrl =
      (extras.config?.params?.["apiUrl"] as string | undefined) ?? "http://localhost:8080";
    const baseUrl = apiUrl.replace(/\/$/, "");
    const token = readToken();

    // Flujo proyecto-activo: requiere extras.ide
    if (extras.ide) {
      const agenticConfig = await readAgenticConfigFromIde(extras.ide);
      if (agenticConfig?.project_id) {
        const project = await fetchProjectDetail(baseUrl, agenticConfig.project_id, token);
        if (project) {
          const items = buildProjectContextItems(project);
          if (items.length > 0) return items;
        }
      }
    }

    // Fallback: catálogo global de la org
    const [standardsRes, patternsRes] = await Promise.allSettled([
      fetchJson(`${baseUrl}/api/v1/coding_standards/`, token),
      fetchJson(`${baseUrl}/api/v1/design_patterns/`, token),
    ]);

    const items: ContextItem[] = [];

    if (standardsRes.status === "fulfilled") {
      extractItems(standardsRes.value).forEach((i) =>
        items.push(itemToContextItem("Standard", i))
      );
    }

    if (patternsRes.status === "fulfilled") {
      extractItems(patternsRes.value).forEach((i) =>
        items.push(itemToContextItem("Pattern", i))
      );
    }

    return items.length > 0 ? items : loadFallback();
  },
};

export default AgenticStandardsProvider;
