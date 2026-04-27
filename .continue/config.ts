/**
 * Continue.dev programmatic config.
 *
 * Extends config.json (models + context providers) with 3 slash commands:
 *   /generate [use_case]  — streams code generation via DevCore Agent SSE
 *   /validate             — governance analysis of the whole project (Guardian)
 *   /review [file]        — governance analysis of the active file (Guardian)
 *
 * Types are inlined — @continuedev/core is provided by the Continue.dev runtime.
 * Auth: JWT from ~/.agentic/token (DevCore) / GUARDIAN_API_KEY env (Guardian).
 */

// ─── Continue.dev types (provided by runtime, inlined here) ──────────────────

interface ContextItem {
  name: string;
  description: string;
  content: string;
}

interface ChatMessage {
  role: "user" | "assistant" | "system";
  content: string;
}

interface IdeAPI {
  getWorkspaceDirs(): Promise<string[]>;
  getCurrentFile(): Promise<{ path: string; contents: string } | undefined>;
  readFile(filepath: string): Promise<string>;
  fileExists(fileUri: string): Promise<boolean>;
}

interface ContextProviderExtras {
  config?: {
    params?: Record<string, unknown>;
  };
  ide?: IdeAPI;
}

interface ContinueSDK {
  input: string;
  history: ChatMessage[];
  ide: IdeAPI;
  addContextItem(item: ContextItem): void;
}

interface SlashCommand {
  name: string;
  description: string;
  run(sdk: ContinueSDK): AsyncGenerator<string, void, unknown>;
}

interface Config {
  models?: unknown[];
  tabAutocompleteModel?: unknown;
  contextProviders?: unknown[];
  slashCommands?: SlashCommand[];
  customCommands?: unknown[];
  [key: string]: unknown;
}

// ─── Command imports ──────────────────────────────────────────────────────────

import { runGenerate } from "./commands/generate";
import { runValidate } from "./commands/validate";
import { runReview } from "./commands/review";
import { runAutofix } from "./commands/autofix";
import { runApprove } from "./commands/approve";
import { toFsPath, loadEnvFromWorkspace, getApiBaseUrl } from "./commands/shared";
import AgenticStandardsProvider from "./lib/agentic-standards";
import AgenticUseCasesProvider from "./lib/agentic-usecases";

// ─── Workspace resolver ───────────────────────────────────────────────────────

async function getWorkspaceDir(ide: IdeAPI): Promise<string | undefined> {
  try {
    const dirs = await ide.getWorkspaceDirs();
    const raw = dirs[0];
    if (!raw) return undefined;
    return toFsPath(raw);
  } catch {
    return undefined;
  }
}

// ─── Slash command definitions ────────────────────────────────────────────────

const generateCommand: SlashCommand = {
  name: "generate",
  description: "Generate code with DevCore Agent. Usage: /generate [UC001] (omit to see available use cases)",

  async *run(sdk: ContinueSDK): AsyncGenerator<string> {
    const workspaceDir = await getWorkspaceDir(sdk.ide);
    if (!workspaceDir) {
      yield "❌ Could not determine workspace directory.";
      return;
    }
    loadEnvFromWorkspace(workspaceDir);
    const activeFile = await sdk.ide.getCurrentFile().catch(() => undefined);
    yield* runGenerate(workspaceDir, sdk.input, activeFile ?? undefined);
  },
};

const validateCommand: SlashCommand = {
  name: "validate",
  description: "Run Guardian governance analysis on the full project. Usage: /validate",

  async *run(sdk: ContinueSDK): AsyncGenerator<string> {
    const workspaceDir = await getWorkspaceDir(sdk.ide);
    if (!workspaceDir) {
      yield "❌ Could not determine workspace directory.";
      return;
    }
    loadEnvFromWorkspace(workspaceDir);
    yield* runValidate(workspaceDir);
  },
};

const reviewCommand: SlashCommand = {
  name: "review",
  description:
    "Run Guardian governance analysis on the active file. Usage: /review [path/to/file]",

  async *run(sdk: ContinueSDK): AsyncGenerator<string> {
    const workspaceDir = await getWorkspaceDir(sdk.ide);
    if (!workspaceDir) {
      yield "❌ Could not determine workspace directory.";
      return;
    }
    loadEnvFromWorkspace(workspaceDir);
    yield* runReview(workspaceDir, sdk.ide, sdk.input);
  },
};

// ─── modifyConfig ─────────────────────────────────────────────────────────────

interface RegisteredContextProvider {
  name?: string;
  [key: string]: unknown;
}

const autofixCommand: SlashCommand = {
  name: "autofix",
  description: "Auto-fix violations from last /validate run. Usage: /autofix",

  async *run(sdk: ContinueSDK): AsyncGenerator<string> {
    const workspaceDir = await getWorkspaceDir(sdk.ide);
    if (!workspaceDir) {
      yield "❌ Could not determine workspace directory.";
      return;
    }
    loadEnvFromWorkspace(workspaceDir);
    yield* runAutofix(workspaceDir);
  },
};

const approveCommand: SlashCommand = {
  name: "approve",
  description: "Approve or reject the pending DevCore checkpoint. Usage: /approve [feedback] or /approve reject: <reason>",

  async *run(sdk: ContinueSDK): AsyncGenerator<string> {
    const workspaceDir = await getWorkspaceDir(sdk.ide);
    if (!workspaceDir) {
      yield "❌ Could not determine workspace directory.";
      return;
    }
    loadEnvFromWorkspace(workspaceDir);
    yield* runApprove(workspaceDir, sdk.input);
  },
};

export function modifyConfig(config: Config): Config {
  config.slashCommands = [generateCommand, validateCommand, reviewCommand, autofixCommand, approveCommand];

  const existingProviders = (
    (config.contextProviders ?? []) as RegisteredContextProvider[]
  ).filter((p) => p.name !== "agentic-standards" && p.name !== "agentic-usecases");

  const injectApiUrl = (extras: ContextProviderExtras) => ({
    ide: extras?.ide,
    config: { params: { apiUrl: getApiBaseUrl(), ...extras?.config?.params } },
  });

  config.contextProviders = [
    ...existingProviders,
    {
      ...AgenticStandardsProvider,
      getContextItems: (query: string, extras: ContextProviderExtras) =>
        AgenticStandardsProvider.getContextItems(query, injectApiUrl(extras)),
    },
    {
      ...AgenticUseCasesProvider,
      getContextItems: (query: string, extras: ContextProviderExtras) =>
        AgenticUseCasesProvider.getContextItems(query, injectApiUrl(extras)),
    },
  ];

  return config;
}
