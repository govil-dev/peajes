/**
 * /approve [feedback] slash command for Continue.dev.
 *
 * Resumes the DevCore generation after a checkpoint.
 * Usage:
 *   /approve                        — approve as-is, continue
 *   /approve looks good, proceed    — approve with positive feedback
 *   /approve reject: add validation — reject, regenerate with correction
 *
 * Requires a pending checkpoint from a previous /generate call.
 */

import {
  getDevCoreStreamUrl,
  makeBearerHeaders,
  getPendingCheckpoint,
  setPendingCheckpoint,
  clearPendingCheckpoint,
} from "./shared";

// ─── SSE event shapes (same as generate.ts) ───────────────────────────────────

interface SseStatusEvent { message: string }
interface SseCheckpointEvent {
  thread_id?: string;
  routing_reasoning?: string;
  message?: string;
  output?: { files_created?: string[] };
}
interface SseCompletedEvent {
  status: string;
  repo_url?: string;
  current_branch?: string;
  output?: { files_created?: string[] };
}

const MAX_SSE_BUFFER = 10 * 1024 * 1024;

export async function* runApprove(
  workspaceDir: string,
  input: string,
): AsyncGenerator<string> {
  const checkpoint = getPendingCheckpoint();

  if (!checkpoint) {
    yield "❌ No pending checkpoint. Run `/generate <use_case>` first.";
    return;
  }

  const rawFeedback = input.trim();
  const isReject = rawFeedback.toLowerCase().startsWith("reject:");
  const approved = !isReject;
  const feedback = isReject
    ? rawFeedback.slice("reject:".length).trim()
    : rawFeedback;

  yield [
    approved
      ? `✅ Approving checkpoint \`${checkpoint.currentNode}\`...`
      : `🔄 Rejecting checkpoint — regenerating with feedback...`,
    feedback ? `> **Feedback**: ${feedback}` : "",
  ].filter(Boolean).join("\n");

  const streamUrl = getDevCoreStreamUrl();

  let res: Response;
  try {
    res = await fetch(streamUrl, {
      method: "POST",
      headers: makeBearerHeaders(),
      body: JSON.stringify({
        input: {
          is_approved: approved,
          feedback,
        },
        config: { configurable: { thread_id: checkpoint.threadId } },
      }),
      signal: AbortSignal.timeout(10 * 60 * 1000),
    });
  } catch (err) {
    yield `❌ DevCore Agent is not available.\n\n${err instanceof Error ? err.message : String(err)}`;
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

  // Clear pending — will be re-set if another checkpoint is hit
  clearPendingCheckpoint();

  const reader = res.body.getReader();
  const decoder = new TextDecoder();
  let buffer = "";
  let eventType = "message";
  let activeThreadId = checkpoint.threadId;

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
            try { filePath = (JSON.parse(rawData) as { path?: string }).path; } catch { /* ignore */ }
            if (filePath) yield `📄 \`${filePath}\``;
            continue;
          }

          if (currentEvent === "error") {
            let msg = rawData;
            try { msg = (JSON.parse(rawData) as { message?: string }).message ?? rawData; } catch { /* ignore */ }
            yield `❌ Agent error: ${msg}`;
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

            // LLM08: save checkpoint, let developer decide
            setPendingCheckpoint({
              threadId: activeThreadId,
              currentNode: "checkpoint",
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
            return;
          }

          if (currentEvent === "completed" || currentEvent === "message") {
            let parsed: SseCompletedEvent | null = null;
            try { parsed = JSON.parse(rawData) as SseCompletedEvent; } catch { /* ignore */ }

            clearPendingCheckpoint();
            const lines: string[] = ["✅ **Generation completed!**"];
            if (parsed?.repo_url) lines.push(`- **Repo**: ${parsed.repo_url}`);
            if (parsed?.current_branch) lines.push(`- **Branch**: \`${parsed.current_branch}\``);

            const files = parsed?.output?.files_created ?? [];
            if (files.length > 0) {
              lines.push(`\nFiles created (${files.length}):`);
              files.forEach((f) => lines.push(`- \`${f}\``));
            }

            lines.push("\n> Run `agentic check` to validate the generated architecture.");
            yield lines.join("\n");
          }
        }
      }
    }
  } finally {
    reader.cancel();
  }
}
