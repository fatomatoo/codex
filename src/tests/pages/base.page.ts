import { ComponentFixture } from '@angular/core/testing';

export abstract class BasePage<T> {
  constructor(protected readonly fixture: ComponentFixture<T>) {}

  protected root = (): HTMLElement => this.fixture.nativeElement;

  protected selector = (testId: string): string => `[data-testid="${testId}"]`;

  protected query = <E extends HTMLElement>(testId: string): E => {
    const element = this.queryOptional<E>(testId);
    if (!element) {
      throw new Error(`Element mit data-testid "${testId}" nicht gefunden.`);
    }
    return element;
  };

  protected queryOptional = <E extends HTMLElement>(testId: string): E | null =>
    this.root().querySelector<E>(this.selector(testId));

  protected queryAll = <E extends HTMLElement>(testId: string): E[] => [
    ...this.root().querySelectorAll<E>(this.selector(testId)),
  ];
}
