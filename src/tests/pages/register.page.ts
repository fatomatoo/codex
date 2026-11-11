import { BasePage } from './base.page';
import { Button } from '../elements/button';
import { Checkbox } from '../elements/checkbox';
import { SelectField } from '../elements/select-field';
import { TextField } from '../elements/text-field';

export class RegisterPageObject extends BasePage<unknown> {
  private getEmailField = new TextField(() => this.query<HTMLInputElement>('email'));
  private getTicketTypeField = new SelectField(() => this.query<HTMLSelectElement>('ticketType'));
  private getQuantityField = new TextField(() => this.query<HTMLInputElement>('quantity'));
  private getTermsBox = new Checkbox(() => this.query<HTMLInputElement>('acceptTerms'));
  private getSubmitButton = new Button(() => this.query<HTMLButtonElement>('submitButton'));

  public setEmail = (email: string) => this.getEmailField.setValue(email);
  public selectTicketType = (type: 'standard' | 'vip') => this.getTicketTypeField.select(type);
  public setQuantity = (quantity: number) => this.getQuantityField.setValue(String(quantity));
  public acceptTerms = () => this.getTermsBox.check();
  public submit = () => this.getSubmitButton.click();

  public getFormError = (): string | null =>
    this.queryOptional('formError')?.textContent?.trim() ?? null;

  public getTotalPrice = (): number =>
    Number(this.query('totalPrice').textContent?.replace(/[^\d.]/g, ''));
}
