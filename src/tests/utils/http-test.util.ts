import { HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Registration } from '../../app/registration/data/models';

const controller = () => TestBed.inject(HttpTestingController);

export function mockRegistrations(registrations: Registration[] = []) {
  controller().expectOne('/api/registrations').flush(registrations);
}

export function expectCreateRegistration(registration: Registration) {
  controller().expectOne({ method: 'POST', url: '/api/registrations' }).flush(registration);
}

export function expectRemoveRegistration(id: string) {
  controller()
    .expectOne({ method: 'DELETE', url: `/api/registrations/${id}` })
    .flush(null, { status: 204, statusText: 'No Content' });
}

export function failRemoveRegistration(id: string) {
  controller()
    .expectOne({ method: 'DELETE', url: `/api/registrations/${id}` })
    .flush(null, { status: 500, statusText: 'Server Error' });
}

export function verify() {
  controller().verify();
}
