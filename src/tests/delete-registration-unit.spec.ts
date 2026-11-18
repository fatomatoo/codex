import { provideZonelessChangeDetection, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { vi } from 'vitest';
import { RegistrationStore } from '../app/registration/data/registration.store';
import { RegistrationList } from '../app/registration/ui/registration-list/registration-list';
import { ANNA, BEN } from './data/registration.data';

describe('Registrations component (store faked)', () => {
  const registrations = signal([ANNA, BEN]);
  let store = {
    registrations: registrations,
    loading: signal(false),
    loadFailed: signal(false),
    deleteFailed: signal(false),
    load: vi.fn(),
    count: signal(0),
    remove: vi.fn((id: string) => registrations.update((all) => all.filter((r) => r.id !== id))),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideZonelessChangeDetection(),
        provideRouter([]),
        { provide: RegistrationStore, useValue: store },
      ],
    });
  });

  it('removes the clicked registration from the table', async () => {
    const fixture = TestBed.createComponent(RegistrationList);
    await fixture.whenStable();
    fixture.nativeElement.querySelector('[aria-label="ben@example.com löschen"]').click();
    await fixture.whenStable();

    const emails = Array.from<HTMLElement>(
      fixture.nativeElement.querySelectorAll('[data-testid="registrationEmail"]'),
    );
    expect(emails.map((e) => e.textContent?.trim())).toEqual([ANNA.email]);
  });
});
