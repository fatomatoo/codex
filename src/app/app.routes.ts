import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'register' },
  {
    path: 'register',
    title: 'Anmeldung',
    loadComponent: () => import('./register/registration-form').then((m) => m.RegistrationForm),
  },
  {
    path: 'registrations',
    title: 'Anmeldungen',
    loadComponent: () =>
      import('./registrations/registration-list').then((m) => m.RegistrationList),
  },
  { path: '**', redirectTo: 'register' },
];
