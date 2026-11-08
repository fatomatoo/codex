import { ComponentFixture } from '@angular/core/testing';
import { RouterTestingHarness } from '@angular/router/testing';
import { RegisterPage } from '../../app/register/register-page';
import { RegistrationsPage } from '../../app/registrations/registrations-page';

export class RouterTestUtil {
  static async navigateToRegister(): Promise<ComponentFixture<unknown>> {
    const harness = await RouterTestingHarness.create();
    await harness.navigateByUrl('/register', RegisterPage);
    return harness.fixture;
  }

  static async navigateToRegistrations(): Promise<ComponentFixture<unknown>> {
    const harness = await RouterTestingHarness.create();
    await harness.navigateByUrl('/registrations', RegistrationsPage);
    return harness.fixture;
  }
}
