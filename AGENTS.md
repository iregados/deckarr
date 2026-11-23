# AGENTS.md

Rules for AI coding agents working on this repository (**Deckarr**).
Read this file before doing anything. If a user request conflicts with these rules, point out the
conflict and agree on an exception **before** writing code.

---

## Project overview

Deckarr is a Compose Multiplatform app (Android + desktop JVM) for managing media servers and
torrent clients: Radarr (movies), Sonarr (series), qBittorrent and Transmission.

Stack: Kotlin, Compose Material 3, Navigation 3 (`NavKey`), Koin DI, Ktor client, Kotlinx
Serialization, Coil 3, Jetpack DataStore.

Multi-module Gradle layout (shared convention plugins in `build-logic/`):

- `:androidApp` — Android entry point (`MainActivity`, `MainApplication`)
- `:core` (`com.iregados.deckarr.core`) — `theme`, `data` (DataStore DAO, repository
  implementations), `domain` (repository interfaces, DTOs, util), `components` (shared composables),
  `di` (`apiModule`, `repositoryModule`), `util` (extensions, `Notification`)
- `:feature:*` (`com.iregados.deckarr.feature.<name>`) — `:feature:home`, `:feature:movies`,
  `:feature:series`, `:feature:search`, `:feature:downloads`, `:feature:settings`,
  `:feature:navigation`; sub-features nest deeper (e.g. `feature.movies.movie_detail`)
- `:api:*` (`com.iregados.deckarr.api.<name>`) — standalone API layer, app-agnostic:
    - `:api:common` — shared interfaces (`TorrentClientApi`, `TorrentClientConfig`, ...),
      `ApiResult`, `ApiError`, common DTOs, `HttpClientFactory`
    - `:api:radarr`, `:api:sonarr`, `:api:qbittorrent`, `:api:transmission`

---

## Rule 1 — Never write a file larger than 300 lines

- 300 lines is a hard ceiling: imports, blank lines and comments all count.
- Estimate the size **before** writing. If a file would exceed 300 lines, split it and show the
  split in the plan.
- How to split, in order of preference:
    - Extract composables into the feature's `components/` package (one per file).
    - Move data classes / DTOs into their own files.
    - Move helpers into util/extension files (the project uses one-function-per-file utilities like
      `toEtaString.kt`, `hasDatePassed.kt`).
- Never squeeze under the limit by deleting blank lines, comments or error handling — restructure
  instead.

## Rule 2 — Never write multiple files at once

Mandatory workflow for every task:

1. **Plan first.** Before writing any code, post a numbered plan listing every file that will be
   created or modified: what it contains, estimated line count, and the order of writing. Flag any
   file at risk of exceeding 300 lines and explain the split.
2. **Wait.** Do not write anything until the user approves the plan.
3. **One file per turn.** Write exactly one file, then stop. Briefly state what was written, then
   wait for the user to ask for the next file.
4. Never batch edits, never touch "related" files you weren't asked to touch. If a change turns out
   to require modifying another file, stop, update the plan, and wait for approval.
5. Keep the plan visible: after each file, mark it done and show what remains.

## Rule 3 — Stay in pattern (code AND UI)

Match the existing codebase. Do not introduce new libraries, architectures, naming styles or UI
idioms. When unsure how something is done, ask or study the closest existing example and mirror it.

## Rule 4 — Never write or modify files; provide code only

- The agent must NOT create, edit or delete any file directly, even when asked to "fix" something.
- Instead, the agent outputs the proposed code in fenced blocks and states the exact file path and
  the location (function/line/anchor) where it should be pasted.
- The user reviews the code and decides whether and where to apply it.
- If a change seems to require touching another file, the agent lists that file and the needed
  change as a separate snippet instead of editing it.

## Rule 5 — Targeted diffs over full-file rewrites

- Do NOT output an entire modified file unless **more than 50%** of the file has to change (or it is
  a brand-new file).
- When 50% or less of an existing file changes, point to the exact file path and anchor (imports,
  function, class, or line context) and show only what to add, replace, or remove.

## Rule 6 — Keep it simple and follow good practices

- Always choose the simplest implementation that cleanly solves the current requirement.
- Do not over-engineer, introduce speculative abstractions, or build workarounds for hypothetical
  future problems (YAGNI).
- Simplicity never means cutting corners: follow idiomatic Kotlin, Compose, and Android good
  practices (proper state ownership, lifecycle awareness, null safety, clean error handling, and
  framework-native APIs over hacks).

### Code patterns

- **One primary declaration per file**, file name matches the declaration (`DownloadsScreen.kt`
  contains `@Composable fun DownloadsScreen`).
- **Feature structure** — Screen + ViewModel + State + Event + components:
    - `XScreen.kt` — screen composable
    - `XViewModel.kt` — exposes `uiState: StateFlow<XState>` and `onEvent(event: XEvent)`; errors
      are surfaced through `notificationFlow: SharedFlow<Notification>` via `emitError(...)`
    - `XState.kt` — immutable UI state data class
    - `XEvent.kt` — sealed class of user intents
    - `components/` — screen-local composables; dialogs are named `XDialog.kt`
- **Navigation** — `@Serializable data object XKey : NavKey` per screen, keys in `navigation/keys/`
  packages; per-feature entry composables in `feature.<name>.navigation` (`downloadsEntry.kt`, ...);
  shared bottom-nav model in `feature.navigation.home`.
- **Repositories** return `ApiResult<T>` (`Success` / `Error`); call sites map errors to `null` —
  follow this convention.
- **API layer** (`com.iregados.deckarr.api`) — one `XApi` class per service (implementing
  `AutoCloseable`) plus its `XApiConfig`; shared torrent behavior behind `common/interfaces/`;
  internal request builders as `internalXxx.kt` functions under `methods/`; DTOs are
  `@Serializable data class`es; custom parsing via serializers in `serializers/`.
- **DI** — Koin modules per feature (`moviesModule.kt`, `downloadsModule.kt`, `navigationModule.kt`,
  ...) plus `apiModule.kt` and `repositoryModule.kt` in `core.di`. Register ViewModels,
  repositories and API clients in modules; never construct dependencies manually inside composables.
- **Persistence** — DataStore only via `DataStoreDao` / `PreferencesRepository`; keys defined in
  `core.domain.util.SettingsKeys.kt`.
- **Formatting helpers** — top-level extension functions, one per file, named after the function
  (`toFormatedSize.kt`, `toHourAndMinute.kt`). Reuse them; don't re-implement formatting inline.

### UI patterns

- **Material 3 only.** Colors and typography always through `MaterialTheme.colorScheme` /
  `MaterialTheme.typography` — never hard-code hex colors in composables. Custom semantic colors
  exist as theme tokens (`successGreenLight/Dark`, `warningYellowLight/Dark`) defined in
  `core.theme`.
- **Reusable composables** live in `core.components` (`CustomAsyncImage`, `SmartSnackBar`);
  feature-specific ones in the feature's `components/`.
- **Images** — use `CustomAsyncImage` (built-in loading spinner and broken-image fallback), not raw
  `AsyncImage`.
- **Dropdown selectors** — `Box` + clickable surface + `DropdownMenu`/`DropdownMenuItem` (see
  `AddMovieDialog.kt`).
- **Dialogs** — `AlertDialog` with explicit confirm/dismiss `Button`s.
- **Error display** — `SmartSnackBar(viewModel.notificationFlow)`; don't invent new toast/snackbar
  mechanisms.
- **Lists** — `LazyColumn` with stable `key`; a single `expandedItemId`-style state controls row
  expansion (see `DownloadsPager.kt`).
- **Tabs + pager** — `PrimaryTabRow` + `HorizontalPager` synced through state events (see
  `DownloadsPager.kt`).
- **Theming** — everything must work in both light and dark; theme selection via `ThemeOption` /
  `ThemeSelectionComponent`.

---

## Style guardrails

- Kotlin official coding style, 4-space indentation, as in existing files.
- Do not refactor, reformat or "fix" anything outside the task scope — even code that looks wrong.
  Note observations in the plan and let the user decide.
- User-facing strings are plain hard-coded `Text("...")` strings in English — follow suit; do not
  introduce a strings-resource system.
- Keep the existing import style; no wildcard imports.
- No explanatory comments for obvious code; the project uses comments sparingly.
- New dependencies in `build.gradle.kts` require explicit user approval before the plan is executed.

---

## Definition of done

Before presenting a written file, verify:

- [ ] Exactly one file was written in this turn.
- [ ] The file is ≤ 300 lines.
- [ ] File name matches the primary declaration; package matches the folder path.
- [ ] Naming, State/Event/ViewModel, DI and UI conventions match the patterns above.
- [ ] Only theme colors/typography are used; the UI works in light and dark.
- [ ] No unrelated files were touched; no new dependencies were added silently.
- [ ] The plan was updated: file marked done, remaining files listed.
