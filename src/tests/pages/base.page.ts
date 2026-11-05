import { ComponentFixture } from '@angular/core/testing';

export abstract class BasePage<T> {
  constructor(protected readonly fixture: ComponentFixture<T>) {}

  protected root = (): HTMLElement => this.fixture.nativeElement;

  protected query = <E extends HTMLElement>(selector: string): E => {
    const element = this.root().querySelector<E>(selector);
    if (!element) {
      throw new Error(`Element "${selector}" nicht gefunden.`);
    }
    return element;
  };
}
