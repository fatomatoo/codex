import { BasePage } from './base.page';
import { Button } from '../elements/button';
import { Checkbox } from '../elements/checkbox';
import { SelectField } from '../elements/select-field';
import { TextField } from '../elements/text-field';

export class RegisterPageObject extends BasePage<unknown> {
  private getNameField = new TextField(() => this.query<HTMLInputElement>('#name'));
  private getEmailField = new TextField(() => this.query<HTMLInputElement>('#email'));
  private getTicketTypeField = new SelectField(() => this.query<HTMLSelectElement>('#ticketType'));
  private getQuantityField = new TextField(() => this.query<HTMLInputElement>('#quantity'));
  private getTermsBox = new Checkbox(() => this.query<HTMLInputElement>('input[type="checkbox"]'));
  private getSubmitButton = new Button(() =>
    this.query<HTMLButtonElement>('button[type="submit"]'),
  );

  public setName = (name: string) => this.getNameField.setValue(name);
  public setEmail = async (email: string) => {
    this.getEmailField.setValue(email);
    await new Promise((r) => setTimeout(r));
  };
  public selectTicketType = (type: 'standard' | 'vip') => this.getTicketTypeField.select(type);
  public setQuantity = (quantity: number) => this.getQuantityField.setValue(String(quantity));
  public acceptTerms = () => this.getTermsBox.check();
  public submit = async () => {
    this.getSubmitButton.click();
    await new Promise((resolve) => setTimeout(resolve));
  };

  public getTotalPrice = (): number =>
    Number(this.query('[data-testid="totalPrice"]').textContent?.replace(/[^\d.]/g, ''));
}
