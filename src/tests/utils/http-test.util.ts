import { HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Registration } from '../../app/core/models';

export class HttpTestUtil {
  private static controller = () => TestBed.inject(HttpTestingController);

  static mockRegistrations(registrations: Registration[] = []) {
    HttpTestUtil.controller().expectOne('/api/registrations').flush(registrations);
  }

  static mockCreateRegistration(registration: Registration) {
    HttpTestUtil.controller()
      .expectOne({ method: 'POST', url: '/api/registrations' })
      .flush(registration);
  }

  static mockRemoveRegistration(id: string) {
    HttpTestUtil.controller()
      .expectOne({ method: 'DELETE', url: `/api/registrations/${id}` })
      .flush(null, { status: 204, statusText: 'No Content' });
  }

  static verify() {
    HttpTestUtil.controller().verify();
  }
}
