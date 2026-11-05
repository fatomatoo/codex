/** Wrapper für ein Auswahlfeld. */
export class SelectField {
  constructor(private readonly element: () => HTMLSelectElement) {}

  select = (value: string) => {
    const select = this.element();
    select.value = value;
    select.dispatchEvent(new Event('input'));
    select.dispatchEvent(new Event('blur'));
  };

  value = (): string => this.element().value;
}
