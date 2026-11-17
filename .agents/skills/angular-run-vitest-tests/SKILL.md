---
name: angular-run-vitest-tests
description: Use when running the Angular *.spec.ts tests (Vitest browser mode via ng test) in this repo — commands for the full suite, a single spec, and coverage.
---

# Running Vitest Tests

Tests run through the Angular CLI with Vitest in browser mode (Playwright/Chromium).

```bash
# Whole suite, single run
npx ng test --watch=false

# Single spec file
npx ng test --watch=false --include="src/tests/integration-test.spec.ts"

# Coverage
npx ng test --watch=false --coverage
```

- Always pass `--watch=false` in scripts/CI-like runs.
- Do not call `vitest run` directly; it bypasses the Angular build and test setup.
- Failure screenshots land in `.vitest/attachments/`.
- Run the full suite once before finishing; `--include` alone can hide cross-spec state.
