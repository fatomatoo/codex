---
name: angular-write-test-harness-and-page
description: "Use when creating, extending, or reviewing CDK test harnesses and page objects in src/tests (e.g. adding a field accessor or a harness for a new component). Covers harness structure, selector strategy, and page object conventions."
---

# Skill: Write Test Harnesses & Page Objects

Shared infrastructure for `angular-write-integration-test`. Tests never touch the DOM directly; they go through page objects, which go through CDK harnesses.

## Layout

```text
src/tests/harness/   ← CDK harnesses (BaseComponentHarness + one per element/component)
src/tests/pages/     ← page objects (BasePage + one per page/view)
src/tests/utils/     ← TestSetupUtil, RouterTestUtil, HttpTestUtil
src/tests/data/      ← test data
```

## Harness structure

Every harness extends `BaseComponentHarness` (`harness/base-component.harness.ts`), which provides `click()`, `text()` and `with({ id })` (matches `[data-testid]`).

```ts
export class ButtonHarness extends BaseComponentHarness {
  static hostSelector = 'button';

  async isDisabled(): Promise<boolean> {
    return (await this.host()).getProperty<boolean>('disabled');
  }
}
```

- Actions change state (`click`, `remove`); queries read state (`email`, `registrationEmails`).
- Use `locatorFor` for required children, `locatorForOptional` for conditional ones.
- Form-field harnesses should dispatch `blur` after changing a value so touched/validation state is updated.
- Keep the public API minimal — only what tests need. Don't re-implement `click()`/`text()`.
- Repeating structures (table rows) get their own harness scoped to the row (`RegistrationRowHarness`); query children from the row, not from the root loader.

## Selector strategy (priority order)

1. **Component selector / native element** as `hostSelector` (`app-foo`, `button`, `select`).
2. **`data-testid`** via `Harness.with({ id: '...' })` when several elements of the same kind exist. Add the attribute to the template if missing.
3. **Role / label** semantic locators.
4. **CSS** — last resort, anchored to a stable container and justified with a comment.

## Page object conventions

- Extend `BasePage`; constructor takes a `HarnessLoader` (`TestbedHarnessEnvironment.loader(fixture)`).
- One private `get<Field>` per field returning the harness; public methods named after user intent (`removeRegistration(email)`), not UI mechanics (`clickDeleteButtonInRow`).
- All methods are `async`; multi-step interactions are one method.
- **No `expect()` in page objects** — return values, let the test assert.
- Page objects are created via `RouterTestUtil.navigateTo<Page>()`, never by the spec itself.

## Gotchas

- `hostSelector` must match the real element/component selector, otherwise lookups fail.
- Harness calls auto-stabilize the fixture, so no manual `fixture.detectChanges()`/`whenStable()` is needed.

## Checklist

- [ ] Harness extends `BaseComponentHarness` and lives in `src/tests/harness/`.
- [ ] `hostSelector` is the element/component selector; `data-testid` only to disambiguate.
- [ ] Page object extends `BasePage`, is async, has no assertions.
- [ ] New `data-testid` added to the template if required.
