import { HarnessLoader } from '@angular/cdk/testing';
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
    throw new Error(`Anmeldung "${email}" nicht gefunden.`);
  };

  public removeRegistration = async (email: string) => (await this.registration(email)).remove();

  public registrationEmails = async (): Promise<string[]> =>
    Promise.all((await this.registrations()).map((registration) => registration.email()));
}
