import { provideZonelessChangeDetection, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MockProvider, MockRender, ngMocks } from 'ng-mocks';
import { vi } from 'vitest';
import { Registration } from '../app/core/models';
import { ANNA, BEN } from './data/registration.data';
import { RegistrationStore } from '../app/core/registration.store';
import { RegistrationsPage } from '../app/registrations/registrations-page';

describe('Registrations Page (mocked store)', () => {
  const registrations = signal<Registration[]>([]);
  const loading = signal(false);
  const loadFailed = signal(false);
  const deleteFailed = signal(false);
  const store = {
    registrations,
    loading,
    loadFailed,
    deleteFailed,
    count: signal(0),
    load: vi.fn(),
    remove: vi.fn(),
  };

  const render = () => MockRender(RegistrationsPage);
  const rows = (fixture: ReturnType<typeof render>) =>
    ngMocks.findAll(fixture, 'tbody tr').map((row) => ngMocks.formatText(row));

  beforeEach(() => {
    registrations.set([]);
    loading.set(false);
    loadFailed.set(false);
    deleteFailed.set(false);
    store.count.set(0);
    store.load.mockReset();
    store.remove.mockReset();

    TestBed.configureTestingModule({
      imports: [RegistrationsPage],
      providers: [
        provideZonelessChangeDetection(),
        provideRouter([]),
        MockProvider(RegistrationStore, store),
      ],
    });
  });

  it('loads the registrations when the page opens', () => {
    render();

    expect(store.load).toHaveBeenCalledTimes(1);
  });

  it('lists the registrations from the store', () => {
    registrations.set([ANNA, BEN]);
    store.count.set(2);
    const fixture = render();

    const list = rows(fixture);
    expect(list).toHaveLength(2);
    expect(list[0]).toContain('Anna Beispiel');
    expect(list[1]).toContain('Ben Muster');
  });

  it('shows an empty message without registrations', () => {
    const fixture = render();

    expect(ngMocks.formatText(ngMocks.find(fixture, '.empty'))).toBe('Keine Anmeldungen gefunden.');
  });

  it('filters the list by the search query', () => {
    registrations.set([ANNA, BEN]);
    store.count.set(2);
    const fixture = render();

    const query = ngMocks.find<HTMLInputElement>(fixture, '#query');
    query.nativeElement.value = 'ben';
    ngMocks.trigger(query, 'input');
    fixture.detectChanges();

    const list = rows(fixture);
    expect(list).toHaveLength(1);
    expect(list[0]).toContain('Ben Muster');
  });

  it('asks the store to delete the clicked registration', () => {
    registrations.set([ANNA, BEN]);
    store.count.set(2);
    const fixture = render();

    ngMocks.click(ngMocks.find(fixture, '[aria-label="Ben Muster löschen"]'));

    expect(store.remove).toHaveBeenCalledWith('2');
  });

  it('shows an error and retries loading', () => {
    loadFailed.set(true);
    const fixture = render();
    store.load.mockClear();

    expect(ngMocks.formatText(ngMocks.find(fixture, '[role="alert"]'))).toBe(
      'Die Anmeldungen konnten nicht geladen werden.',
    );

    ngMocks.click(ngMocks.find(fixture, 'button'));

    expect(store.load).toHaveBeenCalledTimes(1);
  });
});
