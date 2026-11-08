import { computed, inject } from '@angular/core';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { firstValueFrom } from 'rxjs';
import { NewRegistration, Registration } from './models';
import { RegistrationService } from './registration.service';

interface RegistrationState {
  registrations: Registration[];
  loading: boolean;
  loadFailed: boolean;
  deleteFailed: boolean;
}

const initialState: RegistrationState = {
  registrations: [],
  loading: false,
  loadFailed: false,
  deleteFailed: false,
};

export const RegistrationStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withComputed(({ registrations }) => ({
    count: computed(() => registrations().length),
    revenue: computed(() => registrations().reduce((sum, r) => sum + r.total, 0)),
  })),
  withMethods((store, service = inject(RegistrationService)) => ({
    load(): void {
      patchState(store, { loading: true, loadFailed: false });
      service.getAll().subscribe({
        next: (registrations) => patchState(store, { registrations, loading: false }),
        error: () => patchState(store, { loading: false, loadFailed: true }),
      });
    },

    async add(registration: NewRegistration): Promise<void> {
      const created = await firstValueFrom(service.create(registration));
      patchState(store, (state) => ({ registrations: [...state.registrations, created] }));
    },

    remove(id: string): void {
      patchState(store, { deleteFailed: false });
      service.remove(id).subscribe({
        next: () =>
          patchState(store, (state) => ({
            registrations: state.registrations.filter((r) => r.id !== id),
          })),
        error: () => patchState(store, { deleteFailed: true }),
      });
    },
  })),
);
