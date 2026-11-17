import {
  BaseHarnessFilters,
  ComponentHarness,
  ComponentHarnessConstructor,
  HarnessPredicate,
} from '@angular/cdk/testing';

export interface HarnessFilters extends BaseHarnessFilters {
  id?: string;
}

export abstract class BaseComponentHarness extends ComponentHarness {
  async click(): Promise<void> {
    return (await this.host()).click();
  }

  async text(): Promise<string> {
    return (await (await this.host()).text()).trim();
  }

  static with<T extends BaseComponentHarness>(
    this: ComponentHarnessConstructor<T>,
    options: HarnessFilters = {},
  ): HarnessPredicate<T> {
    return new HarnessPredicate(this, options).addOption(
      'test id',
      options.id,
      async (harness, id) => (await harness.host()).matchesSelector(`[data-testid="${id}"]`),
    );
  }
}
