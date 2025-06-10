import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map, switchMap } from 'rxjs/operators';
import { User } from '../../core/user/user.interface';
import { UserService } from '../../core/user/user.service';
import { ACTION_LOGIN, ACTION_SET_USER, } from './app.actions';

@Injectable()
export class AppEffects {

  private actions$ = inject(Actions);
  private userService = inject(UserService);

  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_LOGIN),
      switchMap(() => this.userService.getLoggedUser()),
      map((user: User) => ACTION_SET_USER({user: user}))
    )
  );
}
