import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { RouterTestingHarness } from '@angular/router/testing';
import { RegistrationsPage } from '../../app/registrations/registrations-page';
import { RegistrationsPageObject } from '../pages/registrations.page';

export class RouterTestUtil {
  static async navigateToRegistrations(): Promise<RegistrationsPageObject> {
    const harness = await RouterTestingHarness.create();
    harness.fixture.autoDetectChanges();
    await harness.navigateByUrl('/registrations', RegistrationsPage);
    return new RegistrationsPageObject(TestbedHarnessEnvironment.loader(harness.fixture));
  }
}
