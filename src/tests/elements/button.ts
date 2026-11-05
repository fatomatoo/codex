/** Wrapper für einen Button. */
export class Button {
  constructor(private readonly element: () => HTMLButtonElement) {}

  click = () => this.element().click();

  isDisabled = (): boolean => this.element().disabled;

  text = (): string => this.element().textContent?.trim() ?? '';
}
