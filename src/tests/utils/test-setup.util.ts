import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { RegistrationForm } from '../../app/register/registration-form';
import { RegistrationList } from '../../app/registrations/registration-list';

export class TestSetupUtil {
  static setupTestEnvironment() {
    TestBed.configureTestingModule({
      providers: [
        provideZonelessChangeDetection(),
        provideRouter([
          { path: 'register', component: RegistrationForm },
          { path: 'registrations', component: RegistrationList },
        ]),
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
  }
}
