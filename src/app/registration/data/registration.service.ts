import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { NewRegistration, Registration } from './models';

@Injectable({ providedIn: 'root' })
export class RegistrationService {
  private readonly http = inject(HttpClient);

  getAll() {
    return this.http.get<Registration[]>('/api/registrations');
  }

  create(registration: NewRegistration) {
    return this.http.post<Registration>('/api/registrations', registration);
  }

  remove(id: string) {
    return this.http.delete<void>(`/api/registrations/${id}`);
  }
}
