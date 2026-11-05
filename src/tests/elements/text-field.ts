/** Wrapper für ein Textfeld (Text, E-Mail, Zahl). */
export class TextField {
  constructor(private readonly element: () => HTMLInputElement) {}

  setValue = (value: string) => {
    const input = this.element();
    input.value = value;
    input.dispatchEvent(new Event('input'));
    input.dispatchEvent(new Event('blur'));
  };

  value = (): string => this.element().value;
}
