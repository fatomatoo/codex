import { ANNA, BEN } from './data/registration.data';
import { RegistrationsPageObject } from './pages/registrations.page';
import {
  expectRemoveRegistration,
  failRemoveRegistration,
  mockRegistrations,
} from './utils/http-test.util';
import { navigateToRegistrations } from './utils/router.util';
import { setupTestEnvironment } from './utils/test-setup.util';

describe('Registrations feature: delete', () => {
  let registrationsPage: RegistrationsPageObject;

  beforeEach(async () => {
    setupTestEnvironment();

    registrationsPage = await navigateToRegistrations();
    mockRegistrations([ANNA, BEN]);
  });

  it('should no longer list a registration when the user deletes it', async () => {
    await registrationsPage.removeRegistration(BEN.email);

    expectRemoveRegistration(BEN.id);
    expect(await registrationsPage.registrationEmails()).toEqual([ANNA.email]);
  });

  it('should keep the registration and show an error when deleting fails', async () => {
    await registrationsPage.removeRegistration(BEN.email);

    failRemoveRegistration(BEN.id);
    expect(await registrationsPage.registrationEmails()).toEqual([ANNA.email, BEN.email]);
    expect(await registrationsPage.deleteErrorMessage()).toBe(
      'The registration could not be deleted.',
    );
  });
});
