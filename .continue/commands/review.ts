/**
 * /review [file] slash command for Continue.dev.
 *
 * POSTs the active file (or the file given as argument) to Guardian Agent
 * /agent/manual_chat. Renders score + violations in the chat.
 */

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

// ─── Continue.dev IDE interface (inlined — no @continuedev/core dependency) ───

interface IdeAPI {
  getWorkspaceDirs(): Promise<string[]>;
  getCurrentFile(): Promise<{ path: string; contents: string } | undefined>;
  readFile(filepath: string): Promise<string>;
}

// ─── Main generator ───────────────────────────────────────────────────────────

export async function* runReview(
  workspaceDir: string,
  ide: IdeAPI,
  input: string
): AsyncGenerator<string> {
  let filePath: string;
  let fileContent: string;

  const trimmedInput = input.trim();

  if (trimmedInput) {
    // File path provided as argument
    filePath = trimmedInput;
    try {
      fileContent = await ide.readFile(filePath);
    } catch {
      yield `❌ Could not read file: \`${filePath}\``;
      return;
    }
  } else {
    // Use the currently open file
    let currentFile: { path: string; contents: string } | undefined;
    try {
      currentFile = await ide.getCurrentFile();
    } catch {
      // getCurrentFile may not be supported in all IDE integrations
    }
    if (!currentFile) {
      yield "❌ No active file open. Open a file or pass a path: `/review src/MyFile.java`";
      return;
    }
    filePath = currentFile.path;
    fileContent = currentFile.contents;
  }

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

  const repo_url = getGitRemote(workspaceDir) ?? "";
  const branch = getGitBranch(workspaceDir);
  const fileName = filePath.split("/").pop() ?? filePath;

  yield `🔍 Reviewing **${fileName}** against ${config.project_code} standards...`;

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
          files_content: [{ path: filePath, content: fileContent }],
        },
        config: { configurable: { thread_id: newThreadId() } },
      }),
      signal: AbortSignal.timeout(5 * 60 * 1000),
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

  yield formatGuardianResult(body);
}
