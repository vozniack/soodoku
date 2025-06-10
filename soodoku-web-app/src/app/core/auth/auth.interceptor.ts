import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { Store } from '@ngrx/store';
import { take, map, switchMap } from 'rxjs';
import { SELECT_USER_STATE } from '../../store/app/app.selectors';
import { UserState } from '../../store/app/app.state';

export const authInterceptorFn: HttpInterceptorFn = (req, next) => {
  const store = inject(Store);

  return store.select(SELECT_USER_STATE).pipe(
    take(1),
    map((state: UserState) => state.token),
    switchMap(token => next(
        token ? req.clone({headers: req.headers.set('Authorization', `Bearer ${token}`)}) : req
      )
    )
  );
};
