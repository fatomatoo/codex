import { BasePage } from './base.page';
import { Button } from '../elements/button';
import { TextField } from '../elements/text-field';

export class RegistrationsPageObject extends BasePage<unknown> {
  private getSearchField = new TextField(() => this.query<HTMLInputElement>('search'));
  private getRetryButton = new Button(() => this.query<HTMLButtonElement>('retryButton'));

  private getRows = () => this.queryAll<HTMLElement>('registrationRow');
  private getRowName = (row: HTMLElement) =>
    row.querySelector(this.selector('registrationName'))?.textContent?.trim() ?? '';
  private getRow = (name: string) => this.getRows().find((row) => this.getRowName(row) === name);
  private getDeleteButton = (name: string) =>
    new Button(() => {
      const button = this.getRow(name)?.querySelector<HTMLButtonElement>(
        this.selector('deleteButton'),
      );
      if (!button) {
        throw new Error(`Anmeldung "${name}" nicht gefunden.`);
      }
      return button;
    });

  public search = (query: string) => this.getSearchField.setValue(query);
  public remove = (name: string) => this.getDeleteButton(name).click();
  public retry = () => this.getRetryButton.click();

  public getNames = (): string[] => this.getRows().map(this.getRowName);
}
