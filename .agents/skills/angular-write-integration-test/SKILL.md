---
name: angular-write-integration-test
description: "Use when writing, extending, or reviewing Angular *.spec.ts integration tests for pages/forms in src/tests — Vitest browser mode with TestBed, real routing and state, mocking only HTTP."
---

# Skill: Write Angular Integration Tests

Specs exercise real routing, real components and real stores. **Only HTTP is mocked** (`HttpTestingController`). The DOM is accessed exclusively through page objects and CDK harnesses (see `angular-write-test-harness-and-page`).

## Structure

```ts
describe('Registrations Page', () => {
  let registrationsPage: RegistrationsPageObject;

  beforeEach(async () => {
    TestSetupUtil.setupTestEnvironment();
    registrationsPage = await RouterTestUtil.navigateToRegistrations();
  });

  afterEach(() => HttpTestUtil.verify());

  it('should no longer list a registration when the user deletes it', async () => {
    // Given
    HttpTestUtil.mockRegistrations([ANNA, BEN]);

    // When
    await registrationsPage.removeRegistration(BEN.email);
    HttpTestUtil.expectRemoveRegistration(BEN.id);

    // Then
    expect(await registrationsPage.registrationEmails()).toEqual([ANNA.email]);
  });
});
```

## Building blocks

- `TestSetupUtil.setupTestEnvironment()` — `provideZonelessChangeDetection()`, real routes, `provideHttpClient()` + `provideHttpClientTesting()`.
- `RouterTestUtil.navigateTo<Page>()` — `RouterTestingHarness`, `fixture.autoDetectChanges(true)`, returns the page object built from `TestbedHarnessEnvironment.loader(fixture)`.
- `HttpTestUtil` — `mock<Resource>` for GETs that only feed data into the page; `expect<Action>` for writes (POST/PUT/DELETE), which assert the request (method, URL, body) via `expectOne` and then `flush` the response; plus `verify()`.
- `data/` — typed test data (`ANNA`, `BEN`).

## Rules

- Navigate via `RouterTestUtil`; never `TestBed.createComponent` the page under test.
- Never use `querySelector`, `nativeElement`, `debugElement` or private component fields in specs.
- Never mock stores/services of the app; mock only the network.
- Always `await` page object calls; harnesses stabilize the fixture, so no `fixture.detectChanges()`, `whenStable()`, `fakeAsync` or `tick`.
- Set up the HTTP expectation after the action that triggers the request, then flush, then assert UI.
- Assert the request body when testing writes (`req.request.body`).
- `describe`/`it` titles describe user-observable behavior: `it('should <outcome> when <condition>')`.
- Add Given/When/Then comments only when a test has several distinct phases.
- A new page needs: page object + `RouterTestUtil.navigateTo<Page>()` + route in `TestSetupUtil`, before the spec.
- Use `vitest` globals (`describe`, `it`, `expect`, `vi`), not Jest.

## Checklist

- [ ] Page object exists and is returned by `RouterTestUtil`.
- [ ] Only HTTP mocked; `HttpTestUtil.verify()` runs after each test.
- [ ] No direct DOM access in the spec; every page-object call awaited.
- [ ] Titles describe behavior; assertions match the title.
- [ ] Tests pass: `npx ng test --watch=false`.
