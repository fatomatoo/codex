import { HarnessLoader } from '@angular/cdk/testing';

export abstract class BasePage {
  constructor(protected readonly harnessLoader: HarnessLoader) {}
}
