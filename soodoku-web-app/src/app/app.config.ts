import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { provideRouter } from '@angular/router';
import { MetaReducer, provideState, provideStore } from '@ngrx/store';

import { routes } from './app.routes';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { appReducer } from './store/app/app.reducers';
import { persistState } from './store/meta/persist.metareducer';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    provideAnimationsAsync(),
    provideState('soodoku-storage', appReducer),
    provideStore(appReducer, {metaReducers: [persistState as MetaReducer]})
  ]
};
