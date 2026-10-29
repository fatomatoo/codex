import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormField, form } from '@angular/forms/signals';
import { RouterLink } from '@angular/router';
import { RegistrationService } from '../core/registration.service';

@Component({
  selector: 'app-registrations-page',
  imports: [FormField, RouterLink, CurrencyPipe, DatePipe],
  templateUrl: './registrations-page.html',
})
export class RegistrationsPage {
  private readonly service = inject(RegistrationService);

  protected readonly registrations = this.service.loadAll();

  protected readonly filter = form(signal({ query: '' }));

  protected readonly visibleRegistrations = computed(() => {
    const query = this.filter.query().value().trim().toLowerCase();
    return this.registrations
      .value()
      .filter(
        (r) => r.name.toLowerCase().includes(query) || r.email.toLowerCase().includes(query),
      );
  });

  protected readonly deleteFailed = signal(false);

  protected remove(id: string): void {
    this.deleteFailed.set(false);
    this.service.remove(id).subscribe({
      next: () => this.registrations.reload(),
      error: () => this.deleteFailed.set(true),
    });
  }
}
