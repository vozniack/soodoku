import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { Store } from '@ngrx/store';
import { take, switchMap, catchError, throwError } from 'rxjs';
import { distinctUntilChanged, tap } from 'rxjs/operators';
import { ACTION_AUTH_LOGIN, ACTION_AUTH_LOGOUT } from '../../store/app/auth/auth.actions';
import { SELECT_AUTH_STATE } from '../../store/app/auth/auth.selectors';
import { AuthService } from './auth.service';

export const authInterceptorFn: HttpInterceptorFn = (req, next) => {
  const store = inject(Store);
  const authService = inject(AuthService);

  return store.select(SELECT_AUTH_STATE).pipe(
    distinctUntilChanged(),
    take(1),
    switchMap(({accessToken, refreshToken}) => {
      const authReq = !req.url.includes('/refresh') && accessToken
        ? req.clone({headers: req.headers.set('Authorization', `Bearer ${accessToken}`)})
        : req;

      return next(authReq).pipe(
        catchError((error: HttpErrorResponse) => {
          const isRefreshing = req.url.includes('/refresh');

          if (error.status === 401 && refreshToken && !isRefreshing) {
            return authService.refresh({refreshToken}).pipe(
              tap(tokens => store.dispatch(ACTION_AUTH_LOGIN({
                accessToken: tokens.accessToken,
                refreshToken: tokens.refreshToken
              }))),
              switchMap(tokens => {
                const retryReq = req.clone({
                  headers: req.headers.set('Authorization', `Bearer ${tokens.accessToken}`)
                });

                return next(retryReq);
              }),
              catchError(err => {
                store.dispatch(ACTION_AUTH_LOGOUT());
                return throwError(() => err);
              })
            );
          }

          return throwError(() => error);
        })
      );
    })
  );
};
