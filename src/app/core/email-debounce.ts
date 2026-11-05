import { InjectionToken } from '@angular/core';

/** Wartezeit in ms, bevor die E-Mail-Adresse nach der Eingabe geprüft wird. */
export const EMAIL_DEBOUNCE_MS = new InjectionToken<number>('EMAIL_DEBOUNCE_MS', {
  providedIn: 'root',
  factory: () => 300,
});
