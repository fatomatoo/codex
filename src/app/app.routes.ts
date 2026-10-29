import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'register' },
  {
    path: 'register',
    title: 'Anmeldung',
    loadComponent: () => import('./register/register-page').then((m) => m.RegisterPage),
  },
  {
    path: 'registrations',
    title: 'Anmeldungen',
    loadComponent: () =>
      import('./registrations/registrations-page').then((m) => m.RegistrationsPage),
  },
  { path: '**', redirectTo: 'register' },
];
