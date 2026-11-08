import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { RegisterPage } from '../../app/register/register-page';
import { RegistrationsPage } from '../../app/registrations/registrations-page';

export class TestSetupUtil {
  static setupTestEnvironment() {
    TestBed.configureTestingModule({
      providers: [
        provideZonelessChangeDetection(),
        provideRouter([
          { path: 'register', component: RegisterPage },
          { path: 'registrations', component: RegistrationsPage },
        ]),
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
  }
}
