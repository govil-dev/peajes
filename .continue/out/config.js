var __create = Object.create;
var __defProp = Object.defineProperty;
var __getOwnPropDesc = Object.getOwnPropertyDescriptor;
var __getOwnPropNames = Object.getOwnPropertyNames;
var __getProtoOf = Object.getPrototypeOf;
var __hasOwnProp = Object.prototype.hasOwnProperty;
var __export = (target, all) => {
  for (var name in all)
    __defProp(target, name, { get: all[name], enumerable: true });
};
var __copyProps = (to, from, except, desc) => {
  if (from && typeof from === "object" || typeof from === "function") {
    for (let key of __getOwnPropNames(from))
      if (!__hasOwnProp.call(to, key) && key !== except)
        __defProp(to, key, { get: () => from[key], enumerable: !(desc = __getOwnPropDesc(from, key)) || desc.enumerable });
  }
  return to;
};
var __toESM = (mod, isNodeMode, target) => (target = mod != null ? __create(__getProtoOf(mod)) : {}, __copyProps(
  // If the importer is in node compatibility mode or this is not an ESM
  // file that has been converted to a CommonJS file using a Babel-
  // compatible transform (i.e. "__esModule" has not been set), then set
  // "default" to the CommonJS "module.exports" for node compatibility.
  isNodeMode || !mod || !mod.__esModule ? __defProp(target, "default", { value: mod, enumerable: true }) : target,
  mod
));
var __toCommonJS = (mod) => __copyProps(__defProp({}, "__esModule", { value: true }), mod);

// config.ts
var config_exports = {};
__export(config_exports, {
  modifyConfig: () => modifyConfig
});
module.exports = __toCommonJS(config_exports);

// commands/shared.ts
var fs = __toESM(require("fs"));
var path = __toESM(require("path"));
var os = __toESM(require("os"));
var child_process = __toESM(require("child_process"));
var nodeCrypto = __toESM(require("crypto"));
var import_url = require("url");
function toFsPath(raw) {
  if (raw.startsWith("file://")) {
    try {
      return (0, import_url.fileURLToPath)(raw);
    } catch {
      return raw.replace(/^file:\/\//, "");
    }
  }
  return raw;
}
var _envCache = /* @__PURE__ */ new Map();
function loadEnvFromWorkspace(workspaceDir) {
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
  }
}
function getEnv(key) {
  return _envCache.get(key) ?? process.env[key];
}
var GUARDIAN_URL_DEFAULT = "https://langchain-guardian-devcore-pihnhikstq-uc.a.run.app/agent/manual_chat";
var DEVCORE_STREAM_URL_DEFAULT = "https://code-generator-pihnhikstq-uc.a.run.app/agent/stream";
function getGuardianUrl() {
  return getEnv("AGENTIC_AGENT_URL") ?? GUARDIAN_URL_DEFAULT;
}
function getDevCoreStreamUrl() {
  return getEnv("DEVCORE_STREAM_URL") ?? DEVCORE_STREAM_URL_DEFAULT;
}
function getApiBaseUrl() {
  const projectsUrl = getEnv("AGENTIC_PROJECTS_URL");
  if (projectsUrl) {
    try {
      return new URL(projectsUrl).origin;
    } catch {
    }
  }
  return "http://localhost:8080";
}
function readToken() {
  try {
    const tokenPath = path.join(os.homedir(), ".agentic", "token");
    const value = fs.readFileSync(tokenPath, "utf-8").trim();
    return value.length > 0 ? value : null;
  } catch {
    return null;
  }
}
function makeBearerHeaders() {
  const headers = { "Content-Type": "application/json" };
  const token = readToken();
  if (token) headers["Authorization"] = `Bearer ${token}`;
  return headers;
}
function makeGuardianHeaders() {
  const headers = { "Content-Type": "application/json" };
  const apiKey = getEnv("GUARDIAN_API_KEY");
  if (apiKey) headers["X-API-Key"] = apiKey;
  return headers;
}
function getGitRemote(cwd) {
  const result = child_process.spawnSync("git", ["remote", "get-url", "origin"], {
    cwd,
    encoding: "utf-8"
  });
  if (result.status !== 0) return null;
  return result.stdout.trim() || null;
}
function getGitBranch(cwd) {
  const result = child_process.spawnSync("git", ["branch", "--show-current"], {
    cwd,
    encoding: "utf-8"
  });
  if (result.status !== 0) return "main";
  return result.stdout.trim() || "main";
}
function readAgenticConfig(workspaceDir) {
  try {
    const raw = fs.readFileSync(path.join(workspaceDir, ".agentic.json"), "utf-8");
    return JSON.parse(raw);
  } catch {
    return null;
  }
}
function readAuthorProfile() {
  try {
    const raw = fs.readFileSync(path.join(os.homedir(), ".agentic", "profile.json"), "utf-8");
    return JSON.parse(raw);
  } catch {
    return null;
  }
}
function newThreadId() {
  return nodeCrypto.randomUUID();
}
function toSnakeCase(str) {
  return str.trim().toLowerCase().replace(/[\s-]+/g, "_");
}
var _pendingCheckpoint = null;
function setPendingCheckpoint(cp) {
  _pendingCheckpoint = cp;
}
function getPendingCheckpoint() {
  return _pendingCheckpoint;
}
function clearPendingCheckpoint() {
  _pendingCheckpoint = null;
}
function isGuardianResponse(value) {
  return typeof value === "object" && value !== null && typeof value["governance_score"] === "number" && typeof value["status"] === "string";
}
function formatGuardianResult(result) {
  const score = result.governance_score;
  const scoreEmoji = score >= 70 ? "\u2705" : score >= 40 ? "\u26A0\uFE0F" : "\u274C";
  const lines = [];
  lines.push(`## ${scoreEmoji} Governance Score: ${score}/100`);
  lines.push(`**Status**: ${result.status}`);
  lines.push(`**Issues**: ${result.stats.total_issues} in ${result.stats.files_affected} file(s)`);
  const allIssues = Object.entries(result.results_by_file).flatMap(
    ([file, issues]) => issues.map((issue) => ({ file, ...issue }))
  );
  if (allIssues.length === 0) {
    lines.push("\n\u2705 No violations found.");
    return lines.join("\n");
  }
  lines.push("\n### Violations\n");
  for (const issue of allIssues) {
    const sevEmoji = issue.severity === "Critical" || issue.severity === "High" ? "\u{1F534}" : "\u{1F7E1}";
    lines.push(`${sevEmoji} **${issue.file}** \u2014 ${issue.type} (${issue.severity})`);
    lines.push(`   ${issue.description}`);
    lines.push(`   \u{1F4A1} ${issue.suggestion}`);
    lines.push("");
  }
  return lines.join("\n");
}

// commands/generate.ts
async function fetchUseCases(projectId, token) {
  const baseUrl = getApiBaseUrl();
  const res = await fetch(
    `${baseUrl}/api/v1/projects/${encodeURIComponent(projectId)}`,
    {
      headers: { "Content-Type": "application/json", Authorization: `Bearer ${token}` },
      signal: AbortSignal.timeout(8e3)
    }
  );
  if (!res.ok) return [];
  const data = await res.json();
  return data.body?.domain_definition?.use_case ?? [];
}
async function sendContext(threadId, context, activeFilePath, activeFileContent, token) {
  const streamUrl = getDevCoreStreamUrl();
  let contextUrl;
  try {
    contextUrl = `${new URL(streamUrl).origin}/agent/context/${encodeURIComponent(threadId)}`;
  } catch {
    return;
  }
  const headers = makeBearerHeaders();
  await fetch(contextUrl, {
    method: "POST",
    headers,
    body: JSON.stringify({ context, active_file_path: activeFilePath, active_file_content: activeFileContent }),
    signal: AbortSignal.timeout(8e3)
  }).catch(() => {
  });
}
var MAX_SSE_BUFFER = 10 * 1024 * 1024;
async function* runGenerate(workspaceDir, useCase, activeFile, _resumeThreadId) {
  const config = readAgenticConfig(workspaceDir);
  if (!config) {
    yield "\u274C `.agentic.json` not found. Run `agentic init` in your project first.";
    return;
  }
  const token = readToken();
  if (!token) {
    yield "\u274C Not authenticated. Run `agentic login` in the terminal first.";
    return;
  }
  const projectId = config.project_id ?? config.project_code;
  let trimmedUseCase = useCase.trim();
  if (!trimmedUseCase) {
    yield "\u{1F50D} Fetching use cases for this project...";
    let useCases = [];
    try {
      useCases = await fetchUseCases(projectId, token);
    } catch {
    }
    if (!useCases.length) {
      yield "\u274C No use cases found for this project. Provide the ID manually: `/generate UC001`";
      return;
    }
    const list = useCases.map((uc) => `- \`${uc.code}\` \u2014 ${uc.title}${uc.complexity ? ` _(${uc.complexity})_` : ""}`).join("\n");
    yield `## Available use cases

${list}

Run \`/generate <code>\` to start, e.g. \`/generate ${useCases[0].code}\``;
    return;
  }
  const branchWorking = `feature/${toSnakeCase(trimmedUseCase)}`;
  const threadId = newThreadId();
  yield [
    `\u{1F916} Starting DevCore Agent...`,
    `- **Project**: ${config.project_code}`,
    `- **Branch**: \`${branchWorking}\``,
    `- **Use case**: \`${trimmedUseCase}\``,
    `- **Mode**: SSE streaming`
  ].join("\n");
  const streamUrl = getDevCoreStreamUrl();
  let res;
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
          working_directory: workspaceDir
        },
        config: { configurable: { thread_id: threadId } }
      }),
      signal: AbortSignal.timeout(10 * 60 * 1e3)
    });
  } catch (err) {
    yield `\u274C DevCore Agent is not available at \`${streamUrl}\`.

${err instanceof Error ? err.message : String(err)}`;
    return;
  }
  if (res.status === 404 || res.status === 405) {
    yield `\u274C SSE endpoint not available (${res.status}). Make sure DevCore Agent is running.`;
    return;
  }
  if (!res.ok) {
    yield `\u274C DevCore error: ${res.status} ${res.statusText}`;
    return;
  }
  if (!res.body) {
    yield "\u274C Empty response from DevCore Agent.";
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
        yield "\u274C SSE buffer exceeded limit.";
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
              msg = JSON.parse(rawData).message ?? rawData;
            } catch {
            }
            yield `\u274C Agent error: ${msg}`;
            continue;
          }
          if (currentEvent === "context_request") {
            let parsed = null;
            try {
              parsed = JSON.parse(rawData);
            } catch {
            }
            const ctxThreadId = parsed?.thread_id ?? activeThreadId;
            if (!/^[0-9a-f-]{36}$/i.test(ctxThreadId)) continue;
            if ((parsed?.timeout_seconds ?? 0) > 0 && activeFile?.contents) {
              const truncatedContent = activeFile.contents.slice(0, 8e3);
              yield `\u{1F4AC} Sending active file as context: \`${activeFile.path}\` (${truncatedContent.length} chars)`;
              await sendContext(ctxThreadId, "", activeFile.path, truncatedContent);
            }
            continue;
          }
          if (currentEvent === "status") {
            let parsed = null;
            try {
              parsed = JSON.parse(rawData);
            } catch {
            }
            yield `\u27F3 ${parsed?.message ?? rawData}`;
            continue;
          }
          if (currentEvent === "tests_generated") {
            yield "\u{1F9EA} Tests generated.";
            continue;
          }
          if (currentEvent === "diagram_ready") {
            yield "\u{1F4D0} Architecture diagram ready at `docs/architecture.mmd`.";
            continue;
          }
          if (currentEvent === "guardian_result") {
            yield "\u{1F6E1}\uFE0F Guardian governance check complete.";
            continue;
          }
          if (currentEvent === "file_ready") {
            let filePath;
            try {
              filePath = JSON.parse(rawData).path;
            } catch {
            }
            if (filePath) yield `\u{1F4C4} \`${filePath}\``;
            continue;
          }
          if (currentEvent === "checkpoint") {
            let parsed = null;
            try {
              parsed = JSON.parse(rawData);
            } catch {
            }
            if (parsed?.thread_id) activeThreadId = parsed.thread_id;
            const reason = parsed?.routing_reasoning ?? parsed?.message ?? "Checkpoint reached";
            const files = parsed?.output?.files_created ?? [];
            const fileList = files.length > 0 ? `

Files generated so far:
${files.map((f) => `- \`${f}\``).join("\n")}` : "";
            setPendingCheckpoint({
              threadId: activeThreadId,
              currentNode: parsed?.routing_reasoning ? "checkpoint" : "checkpoint",
              summary: reason,
              files,
              workspaceDir
            });
            yield [
              `\u{1F4CB} **Checkpoint reached**: ${reason}${fileList}`,
              "",
              `> **Review the generated files, then:**`,
              `> - Type \`/approve\` to continue`,
              `> - Type \`/approve <feedback>\` to continue with corrections`,
              `> - Type \`/approve reject: <reason>\` to reject and regenerate`
            ].join("\n");
            return;
          }
          if (currentEvent === "completed" || currentEvent === "message") {
            let parsed = null;
            try {
              parsed = JSON.parse(rawData);
            } catch {
            }
            clearPendingCheckpoint();
            const completionLines = ["\u2705 **Generation completed!**"];
            if (parsed?.repo_url) completionLines.push(`- **Repo**: ${parsed.repo_url}`);
            if (parsed?.current_branch) completionLines.push(`- **Branch**: \`${parsed.current_branch}\``);
            const files = parsed?.output?.files_created ?? [];
            if (files.length > 0) {
              completionLines.push(`
Files created (${files.length}):`);
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

// commands/validate.ts
var fs2 = __toESM(require("fs"));
var path2 = __toESM(require("path"));
var CODE_EXTENSIONS = /* @__PURE__ */ new Set([
  ".java",
  ".go",
  ".ts",
  ".py",
  ".kt",
  ".xml",
  ".yaml",
  ".yml",
  ".json"
]);
var EXCLUDED_DIRS = /* @__PURE__ */ new Set([
  "node_modules",
  ".git",
  "target",
  "build",
  "dist",
  "__pycache__",
  ".next",
  ".venv",
  "venv"
]);
var MAX_FILES = 50;
var MAX_FILE_BYTES = 50 * 1024;
function collectFiles(dir, relBase, results) {
  if (results.length >= MAX_FILES) return;
  let entries;
  try {
    entries = fs2.readdirSync(dir, { withFileTypes: true });
  } catch {
    return;
  }
  for (const entry of entries) {
    if (results.length >= MAX_FILES) break;
    if (EXCLUDED_DIRS.has(entry.name)) continue;
    const full = path2.join(dir, entry.name);
    const rel = relBase ? `${relBase}/${entry.name}` : entry.name;
    if (entry.isDirectory()) {
      collectFiles(full, rel, results);
    } else if (entry.isFile() && CODE_EXTENSIONS.has(path2.extname(entry.name).toLowerCase())) {
      try {
        const stat = fs2.statSync(full);
        if (stat.size > MAX_FILE_BYTES) continue;
        results.push({ path: rel, content: fs2.readFileSync(full, "utf-8") });
      } catch {
      }
    }
  }
}
async function* runValidate(workspaceDir) {
  const config = readAgenticConfig(workspaceDir);
  if (!config) {
    yield "\u274C `.agentic.json` not found. Run `agentic init` in your project first.";
    return;
  }
  const profile = readAuthorProfile();
  if (!profile) {
    yield "\u274C Author profile not found. Run `agentic login` in the terminal first.";
    return;
  }
  const repo_url = getGitRemote(workspaceDir);
  if (!repo_url) {
    yield "\u274C No git remote `origin` detected in this workspace.";
    return;
  }
  const branch = getGitBranch(workspaceDir);
  yield `\u{1F50D} Analyzing **${config.project_code}** (branch: \`${branch}\`)...

Collecting files...`;
  const files_content = [];
  collectFiles(workspaceDir, "", files_content);
  yield `\u{1F4E6} Sending ${files_content.length} file(s) to Guardian Agent...`;
  const guardianUrl = getGuardianUrl();
  let res;
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
          ...files_content.length > 0 ? { files_content } : {}
        },
        config: { configurable: { thread_id: newThreadId() } }
      }),
      signal: AbortSignal.timeout(10 * 60 * 1e3)
    });
  } catch (err) {
    yield `\u274C Guardian Agent is not available at \`${guardianUrl}\`.

Make sure it's running.

${err instanceof Error ? err.message : String(err)}`;
    return;
  }
  if (!res.ok) {
    yield `\u274C Guardian error: ${res.status} ${res.statusText}`;
    return;
  }
  let data;
  try {
    data = await res.json();
  } catch {
    yield "\u274C Guardian returned an invalid JSON response.";
    return;
  }
  const body = data["body"] ?? data;
  if (!isGuardianResponse(body)) {
    yield "\u274C Unexpected Guardian response format (missing `governance_score` or `status`).";
    return;
  }
  try {
    fs2.writeFileSync(
      path2.join(workspaceDir, ".agentic-guardian-result.json"),
      JSON.stringify(body, null, 2),
      "utf-8"
    );
  } catch {
  }
  yield formatGuardianResult(body);
}

// commands/review.ts
async function* runReview(workspaceDir, ide, input) {
  let filePath;
  let fileContent;
  const trimmedInput = input.trim();
  if (trimmedInput) {
    filePath = trimmedInput;
    try {
      fileContent = await ide.readFile(filePath);
    } catch {
      yield `\u274C Could not read file: \`${filePath}\``;
      return;
    }
  } else {
    let currentFile;
    try {
      currentFile = await ide.getCurrentFile();
    } catch {
    }
    if (!currentFile) {
      yield "\u274C No active file open. Open a file or pass a path: `/review src/MyFile.java`";
      return;
    }
    filePath = currentFile.path;
    fileContent = currentFile.contents;
  }
  const config = readAgenticConfig(workspaceDir);
  if (!config) {
    yield "\u274C `.agentic.json` not found. Run `agentic init` in your project first.";
    return;
  }
  const profile = readAuthorProfile();
  if (!profile) {
    yield "\u274C Author profile not found. Run `agentic login` in the terminal first.";
    return;
  }
  const repo_url = getGitRemote(workspaceDir) ?? "";
  const branch = getGitBranch(workspaceDir);
  const fileName = filePath.split("/").pop() ?? filePath;
  yield `\u{1F50D} Reviewing **${fileName}** against ${config.project_code} standards...`;
  const guardianUrl = getGuardianUrl();
  let res;
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
          files_content: [{ path: filePath, content: fileContent }]
        },
        config: { configurable: { thread_id: newThreadId() } }
      }),
      signal: AbortSignal.timeout(5 * 60 * 1e3)
    });
  } catch (err) {
    yield `\u274C Guardian Agent is not available at \`${guardianUrl}\`.

Make sure it's running.

${err instanceof Error ? err.message : String(err)}`;
    return;
  }
  if (!res.ok) {
    yield `\u274C Guardian error: ${res.status} ${res.statusText}`;
    return;
  }
  let data;
  try {
    data = await res.json();
  } catch {
    yield "\u274C Guardian returned an invalid JSON response.";
    return;
  }
  const body = data["body"] ?? data;
  if (!isGuardianResponse(body)) {
    yield "\u274C Unexpected Guardian response format (missing `governance_score` or `status`).";
    return;
  }
  yield formatGuardianResult(body);
}

// commands/autofix.ts
var fs3 = __toESM(require("fs"));
var path3 = __toESM(require("path"));
function getAutofixUrl() {
  const streamUrl = getDevCoreStreamUrl();
  try {
    return `${new URL(streamUrl).origin}/agent/autofix`;
  } catch {
    return "https://devcore.duckdns.org/agent/autofix";
  }
}
function readLastGuardianResult(workspaceDir) {
  try {
    const raw = fs3.readFileSync(
      path3.join(workspaceDir, ".agentic-guardian-result.json"),
      "utf-8"
    );
    return JSON.parse(raw);
  } catch {
    return null;
  }
}
var MAX_SSE_BUFFER2 = 10 * 1024 * 1024;
async function* runAutofix(workspaceDir) {
  loadEnvFromWorkspace(workspaceDir);
  const config = readAgenticConfig(workspaceDir);
  if (!config) {
    yield "\u274C `.agentic.json` not found. Run `agentic init` in your project first.";
    return;
  }
  const lastResult = readLastGuardianResult(workspaceDir);
  if (!lastResult) {
    yield "\u274C No Guardian result found. Run `/validate` first to detect violations.";
    return;
  }
  const violations = Object.entries(
    lastResult.results_by_file ?? {}
  ).flatMap(
    ([file, issues]) => issues.map((issue) => ({
      file,
      type: issue.type,
      severity: issue.severity,
      description: issue.description,
      suggestion: issue.suggestion
    }))
  );
  if (!violations.length) {
    yield "\u2705 No violations to fix. Your code is clean!";
    return;
  }
  yield [
    `\u{1F527} **Autofix** \u2014 ${violations.length} violation(s) in ${config.project_code}`,
    `- **Working dir**: \`${workspaceDir}\``,
    `- Sending to DevCore Agent...`
  ].join("\n");
  const autofixUrl = getAutofixUrl();
  let res;
  try {
    res = await fetch(autofixUrl, {
      method: "POST",
      headers: makeBearerHeaders(),
      body: JSON.stringify({
        working_directory: workspaceDir,
        project_code: config.project_code,
        violations
      }),
      signal: AbortSignal.timeout(10 * 60 * 1e3)
    });
  } catch (err) {
    yield `\u274C DevCore Agent not available at \`${autofixUrl}\`.

${err instanceof Error ? err.message : String(err)}`;
    return;
  }
  if (!res.ok) {
    yield `\u274C Autofix error: ${res.status} ${res.statusText}`;
    return;
  }
  if (!res.body) {
    yield "\u274C Empty response from DevCore Agent.";
    return;
  }
  const reader = res.body.getReader();
  const decoder = new TextDecoder();
  let buffer = "";
  let eventType = "message";
  const fixedFiles = [];
  try {
    while (true) {
      const { done, value } = await reader.read();
      if (done) break;
      buffer += decoder.decode(value, { stream: true });
      if (buffer.length > MAX_SSE_BUFFER2) {
        reader.cancel();
        yield "\u274C SSE buffer exceeded limit.";
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
              msg = JSON.parse(rawData).message ?? rawData;
            } catch {
            }
            yield `\u274C Agent error: ${msg}`;
            return;
          }
          if (currentEvent === "status") {
            let parsed = null;
            try {
              parsed = JSON.parse(rawData);
            } catch {
            }
            yield `\u27F3 ${parsed?.message ?? rawData}`;
            continue;
          }
          if (currentEvent === "file_ready") {
            let filePath;
            try {
              filePath = JSON.parse(rawData).file_path;
            } catch {
            }
            if (filePath && !filePath.includes("..") && !filePath.startsWith("/")) {
              fixedFiles.push(filePath);
              yield `\u270F\uFE0F Fixed: \`${filePath}\``;
            }
            continue;
          }
          if (currentEvent === "completed") {
            let parsed = null;
            try {
              parsed = JSON.parse(rawData);
            } catch {
            }
            const summary = [
              parsed?.status === "error" ? `\u26A0\uFE0F Autofix finished with errors. ${parsed?.message ?? ""}` : `\u2705 **Autofix completed!** ${parsed?.message ?? ""}`
            ];
            if (fixedFiles.length) {
              summary.push(`
${fixedFiles.length} file(s) corrected:`);
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

// commands/approve.ts
var MAX_SSE_BUFFER3 = 10 * 1024 * 1024;
async function* runApprove(workspaceDir, input) {
  const checkpoint = getPendingCheckpoint();
  if (!checkpoint) {
    yield "\u274C No pending checkpoint. Run `/generate <use_case>` first.";
    return;
  }
  const rawFeedback = input.trim();
  const isReject = rawFeedback.toLowerCase().startsWith("reject:");
  const approved = !isReject;
  const feedback = isReject ? rawFeedback.slice("reject:".length).trim() : rawFeedback;
  yield [
    approved ? `\u2705 Approving checkpoint \`${checkpoint.currentNode}\`...` : `\u{1F504} Rejecting checkpoint \u2014 regenerating with feedback...`,
    feedback ? `> **Feedback**: ${feedback}` : ""
  ].filter(Boolean).join("\n");
  const streamUrl = getDevCoreStreamUrl();
  let res;
  try {
    res = await fetch(streamUrl, {
      method: "POST",
      headers: makeBearerHeaders(),
      body: JSON.stringify({
        input: {
          is_approved: approved,
          feedback
        },
        config: { configurable: { thread_id: checkpoint.threadId } }
      }),
      signal: AbortSignal.timeout(10 * 60 * 1e3)
    });
  } catch (err) {
    yield `\u274C DevCore Agent is not available.

${err instanceof Error ? err.message : String(err)}`;
    return;
  }
  if (!res.ok) {
    yield `\u274C DevCore error: ${res.status} ${res.statusText}`;
    return;
  }
  if (!res.body) {
    yield "\u274C Empty response from DevCore Agent.";
    return;
  }
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
      if (buffer.length > MAX_SSE_BUFFER3) {
        reader.cancel();
        yield "\u274C SSE buffer exceeded limit.";
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
            let parsed = null;
            try {
              parsed = JSON.parse(rawData);
            } catch {
            }
            yield `\u27F3 ${parsed?.message ?? rawData}`;
            continue;
          }
          if (currentEvent === "tests_generated") {
            yield "\u{1F9EA} Tests generated.";
            continue;
          }
          if (currentEvent === "diagram_ready") {
            yield "\u{1F4D0} Architecture diagram ready at `docs/architecture.mmd`.";
            continue;
          }
          if (currentEvent === "guardian_result") {
            yield "\u{1F6E1}\uFE0F Guardian governance check complete.";
            continue;
          }
          if (currentEvent === "file_ready") {
            let filePath;
            try {
              filePath = JSON.parse(rawData).path;
            } catch {
            }
            if (filePath) yield `\u{1F4C4} \`${filePath}\``;
            continue;
          }
          if (currentEvent === "error") {
            let msg = rawData;
            try {
              msg = JSON.parse(rawData).message ?? rawData;
            } catch {
            }
            yield `\u274C Agent error: ${msg}`;
            continue;
          }
          if (currentEvent === "checkpoint") {
            let parsed = null;
            try {
              parsed = JSON.parse(rawData);
            } catch {
            }
            if (parsed?.thread_id) activeThreadId = parsed.thread_id;
            const reason = parsed?.routing_reasoning ?? parsed?.message ?? "Checkpoint reached";
            const files = parsed?.output?.files_created ?? [];
            const fileList = files.length > 0 ? `

Files generated so far:
${files.map((f) => `- \`${f}\``).join("\n")}` : "";
            setPendingCheckpoint({
              threadId: activeThreadId,
              currentNode: "checkpoint",
              summary: reason,
              files,
              workspaceDir
            });
            yield [
              `\u{1F4CB} **Checkpoint reached**: ${reason}${fileList}`,
              "",
              `> **Review the generated files, then:**`,
              `> - Type \`/approve\` to continue`,
              `> - Type \`/approve <feedback>\` to continue with corrections`,
              `> - Type \`/approve reject: <reason>\` to reject and regenerate`
            ].join("\n");
            return;
          }
          if (currentEvent === "completed" || currentEvent === "message") {
            let parsed = null;
            try {
              parsed = JSON.parse(rawData);
            } catch {
            }
            clearPendingCheckpoint();
            const lines2 = ["\u2705 **Generation completed!**"];
            if (parsed?.repo_url) lines2.push(`- **Repo**: ${parsed.repo_url}`);
            if (parsed?.current_branch) lines2.push(`- **Branch**: \`${parsed.current_branch}\``);
            const files = parsed?.output?.files_created ?? [];
            if (files.length > 0) {
              lines2.push(`
Files created (${files.length}):`);
              files.forEach((f) => lines2.push(`- \`${f}\``));
            }
            lines2.push("\n> Run `agentic check` to validate the generated architecture.");
            yield lines2.join("\n");
          }
        }
      }
    }
  } finally {
    reader.cancel();
  }
}

// lib/agentic-standards.ts
var fs4 = __toESM(require("fs"));
var path4 = __toESM(require("path"));
var os2 = __toESM(require("os"));
var import_url2 = require("url");
var FALLBACK_PATH = path4.join(__dirname, "..", "standards.json");
function loadFallback() {
  try {
    const raw = fs4.readFileSync(FALLBACK_PATH, "utf-8");
    const items = JSON.parse(raw);
    return items.map((s) => ({
      name: s.title,
      description: `[${s.category}] ${s.language}`,
      content: `## ${s.title}
${s.description}`
    }));
  } catch {
    return [];
  }
}
function readToken2() {
  try {
    const tokenPath = path4.join(os2.homedir(), ".agentic", "token");
    const value = fs4.readFileSync(tokenPath, "utf-8").trim();
    return value.length > 0 ? value : null;
  } catch {
    return null;
  }
}
function extractItems(json) {
  const body = json?.body;
  if (body == null) return [];
  if (Array.isArray(body)) return body;
  if (Array.isArray(body.items)) return body.items;
  return [];
}
function itemToContextItem(prefix, item) {
  const name = item.title ?? item.name ?? item.id ?? "Standard";
  const description = item.description ?? item.rule ?? "";
  return {
    name: `[${prefix}] ${name}`,
    description: prefix,
    content: `## ${name}
${description}`
  };
}
async function fetchJson(url, token) {
  const headers = { "Content-Type": "application/json" };
  if (token) headers["Authorization"] = `Bearer ${token}`;
  const res = await fetch(url, { headers, signal: AbortSignal.timeout(8e3) });
  if (!res.ok) throw new Error(`HTTP ${res.status} from ${url}`);
  const text = await res.text();
  if (!text) return { body: null };
  return JSON.parse(text);
}
async function readAgenticConfigFromIde(ide) {
  try {
    const dirs = await ide.getWorkspaceDirs();
    if (!dirs.length) return null;
    const rawDir = dirs[0];
    const workspaceDir = rawDir.startsWith("file://") ? (0, import_url2.fileURLToPath)(rawDir) : rawDir;
    const configPath = `${workspaceDir}/.agentic.json`;
    const exists = await ide.fileExists(configPath);
    if (!exists) return null;
    const raw = await ide.readFile(configPath);
    return JSON.parse(raw);
  } catch {
    return null;
  }
}
async function fetchProjectDetail(baseUrl, projectId, token) {
  try {
    const url = `${baseUrl}/api/v1/projects/${encodeURIComponent(projectId)}`;
    const headers = { "Content-Type": "application/json" };
    if (token) headers["Authorization"] = `Bearer ${token}`;
    const res = await fetch(url, { headers, signal: AbortSignal.timeout(8e3) });
    if (!res.ok) return null;
    const data = await res.json();
    return data.body ?? null;
  } catch {
    return null;
  }
}
function buildProjectContextItems(project) {
  const items = [];
  const header = `# Proyecto: ${project.name} (${project.code})`;
  if (project.coding_standards?.length) {
    const content = [
      header,
      "## Est\xE1ndares de c\xF3digo",
      ...project.coding_standards.map(
        (s) => `### ${s.name}
${s.description ?? ""}${s.language ? `
_Lenguaje: ${s.language}_` : ""}`
      )
    ].join("\n\n");
    items.push({ name: "Est\xE1ndares de c\xF3digo", description: project.code, content });
  }
  if (project.design_patterns?.length) {
    const content = [
      header,
      "## Patrones de dise\xF1o",
      ...project.design_patterns.map(
        (p) => `### ${p.name}
${p.description ?? ""}${p.implementation_guide ? `

**Gu\xEDa:** ${p.implementation_guide}` : ""}`
      )
    ].join("\n\n");
    items.push({ name: "Patrones de dise\xF1o", description: project.code, content });
  }
  if (project.structure_definition) {
    const s = project.structure_definition;
    const parts = [header, `## Estructura del proyecto
**${s.name}**`];
    if (s.content) parts.push(s.content);
    if (s.package_base_example)
      parts.push(`**Base de paquetes:** \`${s.package_base_example}\``);
    if (s.main_class_pattern)
      parts.push(`**Patr\xF3n de clase principal:** \`${s.main_class_pattern}\``);
    items.push({
      name: "Estructura del proyecto",
      description: project.code,
      content: parts.join("\n\n")
    });
  }
  if (project.infrastructure_definition) {
    const i = project.infrastructure_definition;
    const parts = [header, `## Infraestructura
**${i.name}**`];
    if (i.db_engines?.length) parts.push(`**Bases de datos:** ${i.db_engines.join(", ")}`);
    if (i.api_types?.length) parts.push(`**Tipos de API:** ${i.api_types.join(", ")}`);
    if (i.messaging?.length) parts.push(`**Messaging:** ${i.messaging.join(", ")}`);
    if (i.content) parts.push(i.content);
    items.push({
      name: "Infraestructura",
      description: project.code,
      content: parts.join("\n\n")
    });
  }
  if (project.domain_definition) {
    const d = project.domain_definition;
    const parts = [header, `## Dominio de negocio
**${d.name}**`];
    if (d.description) parts.push(d.description);
    if (d.use_case?.length) {
      parts.push("### Casos de uso activos");
      d.use_case.forEach((uc) => {
        parts.push(
          `- **${uc.code}** \u2014 ${uc.title}${uc.complexity ? ` _(${uc.complexity})_` : ""}${uc.description ? `
  ${uc.description}` : ""}`
        );
      });
    }
    items.push({
      name: "Dominio de negocio",
      description: project.code,
      content: parts.join("\n\n")
    });
  }
  return items;
}
var AgenticStandardsProvider = {
  name: "agentic-standards",
  description: "Coding standards and design patterns from Agentic backend",
  async getContextItems(_query, extras) {
    const apiUrl = extras.config?.params?.["apiUrl"] ?? "http://localhost:8080";
    const baseUrl = apiUrl.replace(/\/$/, "");
    const token = readToken2();
    if (extras.ide) {
      const agenticConfig = await readAgenticConfigFromIde(extras.ide);
      if (agenticConfig?.project_id) {
        const project = await fetchProjectDetail(baseUrl, agenticConfig.project_id, token);
        if (project) {
          const items2 = buildProjectContextItems(project);
          if (items2.length > 0) return items2;
        }
      }
    }
    const [standardsRes, patternsRes] = await Promise.allSettled([
      fetchJson(`${baseUrl}/api/v1/coding_standards/`, token),
      fetchJson(`${baseUrl}/api/v1/design_patterns/`, token)
    ]);
    const items = [];
    if (standardsRes.status === "fulfilled") {
      extractItems(standardsRes.value).forEach(
        (i) => items.push(itemToContextItem("Standard", i))
      );
    }
    if (patternsRes.status === "fulfilled") {
      extractItems(patternsRes.value).forEach(
        (i) => items.push(itemToContextItem("Pattern", i))
      );
    }
    return items.length > 0 ? items : loadFallback();
  }
};
var agentic_standards_default = AgenticStandardsProvider;

// lib/agentic-usecases.ts
var fs5 = __toESM(require("fs"));
var os3 = __toESM(require("os"));
var path5 = __toESM(require("path"));
var import_url3 = require("url");
function readToken3() {
  try {
    const value = fs5.readFileSync(path5.join(os3.homedir(), ".agentic", "token"), "utf-8").trim();
    return value.length > 0 ? value : null;
  } catch {
    return null;
  }
}
async function readAgenticConfigFromIde2(ide) {
  try {
    const dirs = await ide.getWorkspaceDirs();
    if (!dirs.length) return null;
    const rawDir = dirs[0];
    const workspaceDir = rawDir.startsWith("file://") ? (0, import_url3.fileURLToPath)(rawDir) : rawDir;
    const configPath = `${workspaceDir}/.agentic.json`;
    if (!await ide.fileExists(configPath)) return null;
    return JSON.parse(await ide.readFile(configPath));
  } catch {
    return null;
  }
}
async function fetchUseCases2(baseUrl, projectId, token) {
  const headers = { "Content-Type": "application/json" };
  if (token) headers["Authorization"] = `Bearer ${token}`;
  const res = await fetch(
    `${baseUrl}/api/v1/projects/${encodeURIComponent(projectId)}`,
    { headers, signal: AbortSignal.timeout(8e3) }
  );
  if (!res.ok) return [];
  const data = await res.json();
  return data.body?.domain_definition?.use_case ?? [];
}
var AgenticUseCasesProvider = {
  name: "agentic-usecases",
  description: "Use cases of the active project from Agentic backend",
  async getContextItems(_query, extras) {
    const apiUrl = extras.config?.params?.["apiUrl"] ?? "http://localhost:8080";
    const baseUrl = apiUrl.replace(/\/$/, "");
    const token = readToken3();
    if (!extras.ide) return [];
    const agenticConfig = await readAgenticConfigFromIde2(extras.ide);
    const projectId = agenticConfig?.project_id;
    if (!projectId) return [];
    let useCases;
    try {
      useCases = await fetchUseCases2(baseUrl, projectId, token);
    } catch {
      return [];
    }
    if (!useCases.length) return [];
    const header = `# Casos de uso \u2014 ${agenticConfig?.project_code ?? projectId}`;
    const lines = useCases.map((uc) => {
      const complexity = uc.complexity ? ` _(${uc.complexity})_` : "";
      const desc = uc.description ? `
  ${uc.description}` : "";
      return `- **${uc.code}** \u2014 ${uc.title}${complexity}${desc}`;
    });
    return [
      {
        name: `Casos de uso (${useCases.length})`,
        description: agenticConfig?.project_code ?? projectId,
        content: [header, ...lines].join("\n")
      }
    ];
  }
};
var agentic_usecases_default = AgenticUseCasesProvider;

// config.ts
async function getWorkspaceDir(ide) {
  try {
    const dirs = await ide.getWorkspaceDirs();
    const raw = dirs[0];
    if (!raw) return void 0;
    return toFsPath(raw);
  } catch {
    return void 0;
  }
}
var generateCommand = {
  name: "generate",
  description: "Generate code with DevCore Agent. Usage: /generate [UC001] (omit to see available use cases)",
  async *run(sdk) {
    const workspaceDir = await getWorkspaceDir(sdk.ide);
    if (!workspaceDir) {
      yield "\u274C Could not determine workspace directory.";
      return;
    }
    loadEnvFromWorkspace(workspaceDir);
    const activeFile = await sdk.ide.getCurrentFile().catch(() => void 0);
    yield* runGenerate(workspaceDir, sdk.input, activeFile ?? void 0);
  }
};
var validateCommand = {
  name: "validate",
  description: "Run Guardian governance analysis on the full project. Usage: /validate",
  async *run(sdk) {
    const workspaceDir = await getWorkspaceDir(sdk.ide);
    if (!workspaceDir) {
      yield "\u274C Could not determine workspace directory.";
      return;
    }
    loadEnvFromWorkspace(workspaceDir);
    yield* runValidate(workspaceDir);
  }
};
var reviewCommand = {
  name: "review",
  description: "Run Guardian governance analysis on the active file. Usage: /review [path/to/file]",
  async *run(sdk) {
    const workspaceDir = await getWorkspaceDir(sdk.ide);
    if (!workspaceDir) {
      yield "\u274C Could not determine workspace directory.";
      return;
    }
    loadEnvFromWorkspace(workspaceDir);
    yield* runReview(workspaceDir, sdk.ide, sdk.input);
  }
};
var autofixCommand = {
  name: "autofix",
  description: "Auto-fix violations from last /validate run. Usage: /autofix",
  async *run(sdk) {
    const workspaceDir = await getWorkspaceDir(sdk.ide);
    if (!workspaceDir) {
      yield "\u274C Could not determine workspace directory.";
      return;
    }
    loadEnvFromWorkspace(workspaceDir);
    yield* runAutofix(workspaceDir);
  }
};
var approveCommand = {
  name: "approve",
  description: "Approve or reject the pending DevCore checkpoint. Usage: /approve [feedback] or /approve reject: <reason>",
  async *run(sdk) {
    const workspaceDir = await getWorkspaceDir(sdk.ide);
    if (!workspaceDir) {
      yield "\u274C Could not determine workspace directory.";
      return;
    }
    loadEnvFromWorkspace(workspaceDir);
    yield* runApprove(workspaceDir, sdk.input);
  }
};
function modifyConfig(config) {
  config.slashCommands = [generateCommand, validateCommand, reviewCommand, autofixCommand, approveCommand];
  const existingProviders = (config.contextProviders ?? []).filter((p) => p.name !== "agentic-standards" && p.name !== "agentic-usecases");
  const injectApiUrl = (extras) => ({
    ide: extras?.ide,
    config: { params: { apiUrl: getApiBaseUrl(), ...extras?.config?.params } }
  });
  config.contextProviders = [
    ...existingProviders,
    {
      ...agentic_standards_default,
      getContextItems: (query, extras) => agentic_standards_default.getContextItems(query, injectApiUrl(extras))
    },
    {
      ...agentic_usecases_default,
      getContextItems: (query, extras) => agentic_usecases_default.getContextItems(query, injectApiUrl(extras))
    }
  ];
  return config;
}
// Annotate the CommonJS export names for ESM import in node:
0 && (module.exports = {
  modifyConfig
});
