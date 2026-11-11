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

  it('asks the store to delete the clicked registration', () => {
    registrations.set([ANNA, BEN]);
    store.count.set(2);
    const fixture = render();

    ngMocks.click(ngMocks.find(fixture, '[aria-label="ben@example.com löschen"]'));

    expect(store.remove).toHaveBeenCalledWith('2');
  });
});
