import { HttpClient, httpResource } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { NewRegistration, Registration } from './models';

@Injectable({ providedIn: 'root' })
export class RegistrationService {
  private readonly http = inject(HttpClient);

  /** Reactive list of all registrations. Must be called in an injection context. */
  loadAll() {
    return httpResource<Registration[]>(() => '/api/registrations', { defaultValue: [] });
  }

  create(registration: NewRegistration) {
    return this.http.post<Registration>('/api/registrations', registration);
  }

  remove(id: string) {
    return this.http.delete<void>(`/api/registrations/${id}`);
  }
}
