import { provideZonelessChangeDetection, signal } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { MockProvider, MockRender, ngMocks } from 'ng-mocks';
import { vi } from 'vitest';
import { Registration } from '../app/core/models';
import { RegistrationStore } from '../app/core/registration.store';
import { RegistrationsPage } from '../app/registrations/registrations-page';
import { ANNA, BEN } from './data/registration.data';

describe('Registrations Page (Store nachgebaut)', () => {
  const registrations = signal<Registration[]>([]);
  const store = {
    registrations,
    loading: signal(false),
    loadFailed: signal(false),
    deleteFailed: signal(false),
    count: signal(0),
    load: vi.fn(),
    remove: vi.fn((id: string) => registrations.update((all) => all.filter((r) => r.id !== id))),
  };

  beforeEach(() => {
    registrations.set([ANNA, BEN]);
    TestBed.configureTestingModule({
      imports: [RegistrationsPage],
      providers: [
        provideZonelessChangeDetection(),
        provideRouter([]),
        MockProvider(RegistrationStore, store),
      ],
    });
  });

  it('removes the deleted registration from the table', async () => {
    const fixture = MockRender(RegistrationsPage);

    ngMocks.click(ngMocks.find(fixture, '[aria-label="ben@example.com löschen"]'));
    await fixture.whenStable();

    const emails = ngMocks.findAll(fixture, '[data-testid="registrationEmail"]');
    expect(emails.map((e) => e.nativeElement.textContent.trim())).toEqual([ANNA.email]);
  });
});
