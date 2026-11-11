import { ComponentFixture } from '@angular/core/testing';
import { ANNA, BEN } from './data/registration.data';
import { RegistrationsPageObject } from './pages/registrations.page';
import { HttpTestUtil } from './utils/http-test.util';
import { RouterTestUtil } from './utils/router.util';
import { TestSetupUtil } from './utils/test-setup.util';

describe('Registrations List', () => {
  let list: RegistrationsPageObject;
  let fixture: ComponentFixture<unknown>;

  beforeEach(async () => {
    TestSetupUtil.setupTestEnvironment();

    fixture = await RouterTestUtil.navigateToRegistrations();
    list = new RegistrationsPageObject(fixture);
    HttpTestUtil.mockRegistrations([ANNA, BEN]);
    await fixture.whenStable();
  });

  it('removes the deleted registration from the table', async () => {
    list.remove(BEN.email);
    HttpTestUtil.mockRemoveRegistration(BEN.id);
    await fixture.whenStable();

    expect(list.getEmails()).toEqual([ANNA.email]);

    HttpTestUtil.verify();
  });
});
