import { CurrencyPipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import {
  FormField,
  FormRoot,
  email,
  form,
  max,
  min,
  minLength,
  required,
} from '@angular/forms/signals';
import { TicketType } from '../core/models';
import { MAX_TICKETS, calculatePrice } from '../core/price';
import { RegistrationStore } from '../core/registration.store';
import { FieldErrors } from './field-errors';

interface RegistrationFormModel {
  name: string;
  email: string;
  ticketType: TicketType;
  quantity: number;
  acceptTerms: boolean;
}

@Component({
  selector: 'app-register-page',
  imports: [FormRoot, FormField, FieldErrors, CurrencyPipe],
  templateUrl: './register-page.html',
})
export class RegisterPage {
  private readonly store = inject(RegistrationStore);
  private readonly router = inject(Router);

  protected readonly model = signal<RegistrationFormModel>({
    name: '',
    email: '',
    ticketType: 'standard',
    quantity: 1,
    acceptTerms: false,
  });

  protected readonly registrationForm = form(
    this.model,
    (path) => {
      required(path.name, { message: 'Bitte gib deinen Namen an.' });
      minLength(path.name, 2, { message: 'Der Name braucht mindestens 2 Zeichen.' });

      required(path.email, { message: 'Bitte gib deine E-Mail-Adresse an.' });
      email(path.email, { message: 'Das ist keine gültige E-Mail-Adresse.' });

      required(path.quantity, { message: 'Bitte gib die Ticketanzahl an.' });
      min(path.quantity, 1, { message: 'Mindestens 1 Ticket.' });
      max(path.quantity, MAX_TICKETS, { message: `Höchstens ${MAX_TICKETS} Tickets.` });

      required(path.acceptTerms, { message: 'Bitte akzeptiere die Teilnahmebedingungen.' });
    },
    {
      submission: {
        action: async () => {
          const { name, email, ticketType, quantity } = this.model();
          try {
            await this.store.add({ name, email, ticketType, quantity });
          } catch {
            return {
              kind: 'server',
              message: 'Die Anmeldung ist fehlgeschlagen. Bitte versuche es erneut.',
            };
          }
          await this.router.navigate(['/registrations']);
          return undefined;
        },
      },
    },
  );

  protected readonly price = computed(() =>
    calculatePrice(this.model().ticketType, this.model().quantity),
  );
}
