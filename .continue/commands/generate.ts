/**
 * /generate [use_case] slash command for Continue.dev.
 *
 * Sends a POST to DevCore Agent /agent/stream (SSE), streams progress
 * into the chat, and PAUSES at checkpoints for developer approval.
 * Use /approve [feedback] to resume after a checkpoint.
 */

import {
  getDevCoreStreamUrl,
  getDevCoreUrl,
  getApiBaseUrl,
  makeBearerHeaders,
  readAgenticConfig,
  readToken,
  newThreadId,
  toSnakeCase,
  setPendingCheckpoint,
  clearPendingCheckpoint,
} from "./shared";

// ─── Use case fetch ───────────────────────────────────────────────────────────

interface UseCase {
  id: string;
  code: string;
  title: string;
  complexity?: string;
}

async function fetchUseCases(projectId: string, token: string): Promise<UseCase[]> {
  const baseUrl = getApiBaseUrl();
  const res = await fetch(
    `${baseUrl}/api/v1/projects/${encodeURIComponent(projectId)}`,
    {
      headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
      signal: AbortSignal.timeout(8_000),
    }
  );
  if (!res.ok) return [];
  const data = (await res.json()) as { body?: { domain_definition?: { use_case?: UseCase[] } | null } };
  return data.body?.domain_definition?.use_case ?? [];
}

// ─── Context sender (B20) ─────────────────────────────────────────────────────

async function sendContext(
  threadId: string,
  context: string,
  activeFilePath?: string,
  activeFileContent?: string,
  token?: string | null
): Promise<void> {
  const streamUrl = getDevCoreStreamUrl();
  let contextUrl: string;
  try { contextUrl = `${new URL(streamUrl).origin}/agent/context/${encodeURIComponent(threadId)}`; }
  catch { return; }
  const headers = makeBearerHeaders();
  await fetch(contextUrl, {
    method: "POST",
    headers,
    body: JSON.stringify({ context, active_file_path: activeFilePath, active_file_content: activeFileContent }),
    signal: AbortSignal.timeout(8_000),
  }).catch(() => { /* fire-and-forget */ });
}

// ─── SSE event shapes ─────────────────────────────────────────────────────────

interface SseStatusEvent {
  message: string;
  step?: string;
}

interface SseCheckpointEvent {
  thread_id?: string;
  message?: string;
  routing_reasoning?: string;
  output?: { files_created?: string[] };
}

interface SseCompletedEvent {
  status: string;
  repo_url?: string;
  current_branch?: string;
  output?: { files_created?: string[] };
}


// ─── Main generator ───────────────────────────────────────────────────────────

const MAX_SSE_BUFFER = 10 * 1024 * 1024; // 10 MB

export async function* runGenerate(
  workspaceDir: string,
  useCase: string,
  activeFile?: { path: string; contents: string },
  _resumeThreadId?: string,
): AsyncGenerator<string> {
  const config = readAgenticConfig(workspaceDir);
  if (!config) {
    yield "❌ `.agentic.json` not found. Run `agentic init` in your project first.";
    return;
  }

  const token = readToken();
  if (!token) {
    yield "❌ Not authenticated. Run `agentic login` in the terminal first.";
    return;
  }

  const projectId = config.project_id ?? config.project_code;
  let trimmedUseCase = useCase.trim();

  // Sin UC: fetchear lista y mostrarla para que el dev elija
  if (!trimmedUseCase) {
    yield "🔍 Fetching use cases for this project...";
    let useCases: UseCase[] = [];
    try { useCases = await fetchUseCases(projectId, token); } catch { /* ignore */ }

    if (!useCases.length) {
      yield "❌ No use cases found for this project. Provide the ID manually: `/generate UC001`";
      return;
    }

    const list = useCases
      .map((uc) => `- \`${uc.code}\` — ${uc.title}${uc.complexity ? ` _(${uc.complexity})_` : ""}`)
      .join("\n");
    yield `## Available use cases\n\n${list}\n\nRun \`/generate <code>\` to start, e.g. \`/generate ${useCases[0].code}\``;
    return;
  }

  const branchWorking = `feature/${toSnakeCase(trimmedUseCase)}`;
  const threadId = newThreadId();

  yield [
    `🤖 Starting DevCore Agent...`,
    `- **Project**: ${config.project_code}`,
    `- **Branch**: \`${branchWorking}\``,
    `- **Use case**: \`${trimmedUseCase}\``,
    `- **Mode**: SSE streaming`,
  ].join("\n");

  const streamUrl = getDevCoreStreamUrl();

  let res: Response;
  try {
    res = await fetch(streamUrl, {
      method: "POST",
      headers: makeBearerHeaders(),
      body: JSON.stringify({
        input: {
          branch_working: branchWorking,
          project_id: projectId,
          use_case_id: trimmedUseCase,
          is_new_project: false,
          working_directory: workspaceDir,
        },
        config: { configurable: { thread_id: threadId } },
      }),
      signal: AbortSignal.timeout(10 * 60 * 1000),
    });
  } catch (err) {
    yield `❌ DevCore Agent is not available at \`${streamUrl}\`.\n\n${err instanceof Error ? err.message : String(err)}`;
    return;
  }

  if (res.status === 404 || res.status === 405) {
    yield `❌ SSE endpoint not available (${res.status}). Make sure DevCore Agent is running.`;
    return;
  }

  if (!res.ok) {
    yield `❌ DevCore error: ${res.status} ${res.statusText}`;
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
  let activeThreadId = threadId;

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
            try {
              msg = (JSON.parse(rawData) as { message?: string }).message ?? rawData;
            } catch { /* ignore */ }
            yield `❌ Agent error: ${msg}`;
            continue;
          }

          if (currentEvent === "context_request") {
            let parsed: { thread_id?: string; timeout_seconds?: number } | null = null;
            try { parsed = JSON.parse(rawData) as { thread_id?: string; timeout_seconds?: number }; } catch { /* ignore */ }
            // LLM07: validar que thread_id sea UUID antes de usarlo en URL
            const ctxThreadId = parsed?.thread_id ?? activeThreadId;
            if (!/^[0-9a-f-]{36}$/i.test(ctxThreadId)) continue;
            // Auto-send active file as context (no interactive prompt available in Continue)
            if ((parsed?.timeout_seconds ?? 0) > 0 && activeFile?.contents) {
              // LLM03: truncar a 8000 chars para limitar superficie de injection indirecta
              const truncatedContent = activeFile.contents.slice(0, 8000);
              yield `💬 Sending active file as context: \`${activeFile.path}\` (${truncatedContent.length} chars)`;
              await sendContext(ctxThreadId, "", activeFile.path, truncatedContent);
            }
            continue;
          }

          if (currentEvent === "status") {
            let parsed: SseStatusEvent | null = null;
            try { parsed = JSON.parse(rawData) as SseStatusEvent; } catch { /* ignore */ }
            yield `⟳ ${parsed?.message ?? rawData}`;
            continue;
          }

          if (currentEvent === "tests_generated") {
            yield "🧪 Tests generated.";
            continue;
          }

          if (currentEvent === "diagram_ready") {
            yield "📐 Architecture diagram ready at `docs/architecture.mmd`.";
            continue;
          }

          if (currentEvent === "guardian_result") {
            yield "🛡️ Guardian governance check complete.";
            continue;
          }

          if (currentEvent === "file_ready") {
            let filePath: string | undefined;
            try {
              filePath = (JSON.parse(rawData) as { path?: string }).path;
            } catch { /* ignore */ }
            if (filePath) yield `📄 \`${filePath}\``;
            continue;
          }

          if (currentEvent === "checkpoint") {
            let parsed: SseCheckpointEvent | null = null;
            try { parsed = JSON.parse(rawData) as SseCheckpointEvent; } catch { /* ignore */ }

            if (parsed?.thread_id) activeThreadId = parsed.thread_id;

            const reason = parsed?.routing_reasoning ?? parsed?.message ?? "Checkpoint reached";
            const files = parsed?.output?.files_created ?? [];
            const fileList =
              files.length > 0
                ? `\n\nFiles generated so far:\n${files.map((f) => `- \`${f}\``).join("\n")}`
                : "";

            // LLM08: visibilidad antes de agencia — el developer decide explícitamente
            setPendingCheckpoint({
              threadId: activeThreadId,
              currentNode: parsed?.routing_reasoning ? "checkpoint" : "checkpoint",
              summary: reason,
              files,
              workspaceDir,
            });

            yield [
              `📋 **Checkpoint reached**: ${reason}${fileList}`,
              "",
              `> **Review the generated files, then:**`,
              `> - Type \`/approve\` to continue`,
              `> - Type \`/approve <feedback>\` to continue with corrections`,
              `> - Type \`/approve reject: <reason>\` to reject and regenerate`,
            ].join("\n");
            return; // Stop stream — developer decides next step
          }

          if (currentEvent === "completed" || currentEvent === "message") {
            let parsed: SseCompletedEvent | null = null;
            try { parsed = JSON.parse(rawData) as SseCompletedEvent; } catch { /* ignore */ }

            clearPendingCheckpoint();
            const completionLines: string[] = ["✅ **Generation completed!**"];
            if (parsed?.repo_url) completionLines.push(`- **Repo**: ${parsed.repo_url}`);
            if (parsed?.current_branch) completionLines.push(`- **Branch**: \`${parsed.current_branch}\``);

            const files = parsed?.output?.files_created ?? [];
            if (files.length > 0) {
              completionLines.push(`\nFiles created (${files.length}):`);
              files.forEach((f) => completionLines.push(`- \`${f}\``));
            }

            completionLines.push("\n> Run `agentic check` to validate the generated architecture.");
            yield completionLines.join("\n");
          }
        }
      }
    }
  } finally {
    reader.cancel();
  }
}
