import { Component, computed, inject, signal } from '@angular/core';
import { FormField, form } from '@angular/forms/signals';
import { RouterLink } from '@angular/router';
import { RegistrationStore } from '../../data/registration.store';

@Component({
  selector: 'app-registration-list',
  imports: [FormField, RouterLink],
  templateUrl: './registration-list.html',
})
export class RegistrationList {
  protected readonly store = inject(RegistrationStore);

  protected readonly filter = form(signal({ query: '' }));

  protected readonly visibleRegistrations = computed(() => {
    const query = this.filter.query().value().trim().toLowerCase();
    return this.store.registrations().filter((r) => r.email.toLowerCase().includes(query));
  });

  constructor() {
    this.store.load();
  }
}
