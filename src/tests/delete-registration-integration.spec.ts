import { ANNA, BEN } from './data/registration.data';
import { RegistrationsPageObject } from './pages/registrations.page';
import { HttpTestUtil } from './utils/http-test.util';
import { RouterTestUtil } from './utils/router.util';
import { TestSetupUtil } from './utils/test-setup.util';

describe('Registrations feature: delete', () => {
  let registrationsPage: RegistrationsPageObject;

  beforeEach(async () => {
    TestSetupUtil.setupTestEnvironment();

    registrationsPage = await RouterTestUtil.navigateToRegistrations();
    HttpTestUtil.mockRegistrations([ANNA, BEN]);
  });

  it('should no longer list a registration when the user deletes it', async () => {
    await registrationsPage.removeRegistration(BEN.email);

    HttpTestUtil.expectRemoveRegistration(BEN.id);
    expect(await registrationsPage.registrationEmails()).toEqual([ANNA.email]);
  });
});
