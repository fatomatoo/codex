import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideZonelessChangeDetection } from '@angular/core';
import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { RegistrationForm } from '../../app/registration/ui/registration-form/registration-form';
import { RegistrationList } from '../../app/registration/ui/registration-list/registration-list';

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
