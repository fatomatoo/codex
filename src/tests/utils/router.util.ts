import { TestbedHarnessEnvironment } from '@angular/cdk/testing/testbed';
import { RouterTestingHarness } from '@angular/router/testing';
import { RegistrationList } from '../../app/registration/ui/registration-list/registration-list';
import { RegistrationsPageObject } from '../pages/registrations.page';

export class RouterTestUtil {
  static async navigateToRegistrations(): Promise<RegistrationsPageObject> {
    const harness = await RouterTestingHarness.create();
    harness.fixture.autoDetectChanges();
    await harness.navigateByUrl('/registrations', RegistrationList);
    return new RegistrationsPageObject(TestbedHarnessEnvironment.loader(harness.fixture));
  }
}
