import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { Store } from '@ngrx/store';
import { take, map, switchMap } from 'rxjs';
import { SELECT_AUTH_STATE } from '../../store/app/auth/auth.selectors';
import { AuthState } from '../../store/app/auth/auth.state';

export const authInterceptorFn: HttpInterceptorFn = (req, next) => {
  const store = inject(Store);

  return store.select(SELECT_AUTH_STATE).pipe(
    take(1),
    map((authState: AuthState) => authState?.token),
    switchMap((token: string | undefined) => next(
        token ? req.clone({headers: req.headers.set('Authorization', `Bearer ${token}`)}) : req
      )
    )
  );
};
