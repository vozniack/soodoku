import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter } from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { MetaReducer, provideStore } from '@ngrx/store';

import { routes } from './app.routes';
import { provideHttpClient, withInterceptors, withInterceptorsFromDi } from '@angular/common/http';
import { authInterceptorFn } from './core/auth/auth.interceptor';
import { AppEffects } from './store/app/app.effects';
import { appReducers } from './store/app/app.reducers';
import { AuthEffects } from './store/app/auth/auth.effects';
import { GameEffects } from './store/app/game/game.effects';
import { persistState } from './store/meta/meta.reducers';

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
      {soodokuStorage: appReducers},
      {metaReducers: [persistState as MetaReducer]},
    ),
    provideEffects([AppEffects, AuthEffects, GameEffects])
  ]
};
