import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { RegisterPage } from '../../app/register/register-page';

export class RouterTestUtil {
  private static router = () => TestBed.inject(Router);

  static async navigateToRegister(): Promise<ComponentFixture<unknown>> {
    const harness = await RouterTestingHarness.create();
    await harness.navigateByUrl('/register', RegisterPage);
    return harness.fixture;
  }

  static navigateToRegistrations() {
    return RouterTestUtil.router().navigateByUrl('/registrations');
  }
}
