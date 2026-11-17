import { provideZonelessChangeDetection, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { vi } from 'vitest';
import { RegistrationStore } from '../app/core/registration.store';
import { RegistrationsPage } from '../app/registrations/registrations-page';
import { ANNA, BEN } from './data/registration.data';

describe('Registrations Page', () => {
  let store: { remove: ReturnType<typeof vi.fn> };

  beforeEach(() => {
    const registrations = signal([ANNA, BEN]);
    const fakeStore = {
      registrations: registrations,
      loading: signal(false),
      loadFailed: signal(false),
      deleteFailed: signal(false),
      count: signal(0),
      load: vi.fn(),
      remove: vi.fn((id: string) => registrations.update((all) => all.filter((r) => r.id !== id))),
    };
    store = fakeStore;
    TestBed.configureTestingModule({
      providers: [
        provideZonelessChangeDetection(),
        provideRouter([]),
        { provide: RegistrationStore, useValue: fakeStore },
      ],
    });
  });

  it('asks the store to remove the clicked registration', async () => {
    const fixture = TestBed.createComponent(RegistrationsPage);
    await fixture.whenStable();
    fixture.nativeElement.querySelector('[aria-label="ben@example.com löschen"]').click();
    await fixture.whenStable();

    expect(store.remove).toHaveBeenCalledWith(BEN.id);
    const emails = Array.from<HTMLElement>(
      fixture.nativeElement.querySelectorAll('[data-testid="registrationEmail"]'),
    );
    expect(emails.map((e) => e.textContent?.trim())).toEqual([ANNA.email]);
  });
});
