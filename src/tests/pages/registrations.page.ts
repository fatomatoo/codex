import { BasePage } from './base.page';
import { Button } from '../elements/button';
import { TextField } from '../elements/text-field';

export class RegistrationsPageObject extends BasePage<unknown> {
  private getSearchField = new TextField(() => this.query<HTMLInputElement>('search'));
  private getRetryButton = new Button(() => this.query<HTMLButtonElement>('retryButton'));

  private getRows = () => this.queryAll<HTMLElement>('registrationRow');
  private getRowEmail = (row: HTMLElement) =>
    row.querySelector(this.selector('registrationEmail'))?.textContent?.trim() ?? '';
  private getRow = (email: string) => this.getRows().find((row) => this.getRowEmail(row) === email);
  private getDeleteButton = (email: string) =>
    new Button(() => {
      const button = this.getRow(email)?.querySelector<HTMLButtonElement>(
        this.selector('deleteButton'),
      );
      if (!button) {
        throw new Error(`Anmeldung "${email}" nicht gefunden.`);
      }
      return button;
    });

  public search = (query: string) => this.getSearchField.setValue(query);
  public remove = (email: string) => this.getDeleteButton(email).click();
  public retry = () => this.getRetryButton.click();

  public getEmails = (): string[] => this.getRows().map(this.getRowEmail);
}
