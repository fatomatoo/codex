import { HarnessLoader } from '@angular/cdk/testing';
import { AlertHarness } from '../harness/alert.harness';
import { RegistrationRowHarness } from '../harness/registration-row.harness';

export class RegistrationsPageObject {
  constructor(private readonly harnessLoader: HarnessLoader) {}

  private registrations = () => this.harnessLoader.getAllHarnesses(RegistrationRowHarness);

  private registration = async (email: string) => {
    for (const registration of await this.registrations()) {
      if ((await registration.email()) === email) {
        return registration;
      }
    }
    throw new Error(`Registration "${email}" not found.`);
  };

  public removeRegistration = async (email: string) => (await this.registration(email)).remove();

  public registrationEmails = async (): Promise<string[]> =>
    Promise.all((await this.registrations()).map((registration) => registration.email()));

  public deleteErrorMessage = async (): Promise<string | null> => {
    const alert = await this.harnessLoader.getHarnessOrNull(
      AlertHarness.with({ id: 'deleteError' }),
    );
    return alert ? alert.text() : null;
  };
}
