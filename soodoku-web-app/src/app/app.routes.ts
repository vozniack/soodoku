import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'home',
    loadComponent: () => import('./modules/home/home.component').then(m => m.HomeComponent),
  },
  {
    path: '**',
    redirectTo: 'home',
  },
];
