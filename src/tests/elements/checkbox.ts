/** Wrapper für eine Checkbox. */
export class Checkbox {
  constructor(private readonly element: () => HTMLInputElement) {}

  check = () => {
    const checkbox = this.element();
    checkbox.click();
    checkbox.dispatchEvent(new Event('blur'));
  };

  isChecked = (): boolean => this.element().checked;
}
