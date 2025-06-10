import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { MetaReducer, provideStore } from '@ngrx/store';

import { routes } from './app.routes';
import { provideHttpClient, withInterceptors, withInterceptorsFromDi } from '@angular/common/http';
import { authInterceptorFn } from './core/auth/auth.interceptor';
import { AppEffects } from './store/app/app.effects';
import { appReducer } from './store/app/app.reducers';
import { persistState } from './store/meta/persist.metareducer';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(
      withInterceptorsFromDi(),
      withInterceptors([authInterceptorFn])
    ),
    provideAnimationsAsync(),
    provideStore(
      {soodokuStorage: appReducer},
      {metaReducers: [persistState as MetaReducer]},
    ),
    provideEffects(AppEffects)
  ]
};
