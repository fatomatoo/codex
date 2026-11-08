import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormField, form } from '@angular/forms/signals';
import { RouterLink } from '@angular/router';
import { RegistrationStore } from '../core/registration.store';

@Component({
  selector: 'app-registrations-page',
  imports: [FormField, RouterLink, CurrencyPipe, DatePipe],
  templateUrl: './registrations-page.html',
})
export class RegistrationsPage {
  protected readonly store = inject(RegistrationStore);

  protected readonly filter = form(signal({ query: '' }));

  protected readonly visibleRegistrations = computed(() => {
    const query = this.filter.query().value().trim().toLowerCase();
    return this.store
      .registrations()
      .filter((r) => r.name.toLowerCase().includes(query) || r.email.toLowerCase().includes(query));
  });

  constructor() {
    this.store.load();
  }
}
