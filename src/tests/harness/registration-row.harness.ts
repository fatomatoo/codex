import { BaseComponentHarness } from './base-component.harness';
import { ButtonHarness } from './button.harness';

export class RegistrationRowHarness extends BaseComponentHarness {
  static hostSelector = 'tr[data-testid="registrationRow"]';

  private emailCell = this.locatorFor('[data-testid="registrationEmail"]');
  private deleteButton = this.locatorFor(ButtonHarness.with({ id: 'deleteButton' }));

  async email(): Promise<string> {
    return (await this.emailCell()).text();
  }

  async remove(): Promise<void> {
    return (await this.deleteButton()).click();
  }
}
