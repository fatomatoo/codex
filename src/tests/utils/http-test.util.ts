import { HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { NewRegistration, Registration } from '../../app/core/models';

export class HttpTestUtil {
  private static controller = () => TestBed.inject(HttpTestingController);

  static mockCreateRegistration(response: Partial<Registration> = {}): NewRegistration {
    const request = HttpTestUtil.controller().expectOne('/api/registrations');
    request.flush({
      id: '1',
      total: 0,
      createdAt: new Date().toISOString(),
      ...request.request.body,
      ...response,
    } satisfies Partial<Registration>);
    return request.request.body;
  }

  static mockCreateRegistrationError() {
    HttpTestUtil.controller()
      .expectOne('/api/registrations')
      .flush('Fehler', { status: 500, statusText: 'Server Error' });
  }

  static mockRegistrations(registrations: Registration[] = []) {
    HttpTestUtil.controller().expectOne('/api/registrations').flush(registrations);
  }

  static mockRemoveRegistration(id: string) {
    HttpTestUtil.controller()
      .expectOne({ method: 'DELETE', url: `/api/registrations/${id}` })
      .flush(null);
  }

  static verify() {
    HttpTestUtil.controller().verify();
  }
}
