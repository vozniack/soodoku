import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { mergeMap } from 'rxjs';
import { filter, switchMap, tap, withLatestFrom } from 'rxjs/operators';
import { SnackbarService } from '../../core/snackbar/snackbar.service';
import { UserService } from '../../core/user/user.service';
import { ACTION_SET_LANGUAGE, ACTION_SET_THEME, ACTION_SHOW_SNACKBAR } from './app.actions';
import { ACTION_USER_SET } from './user/user.actions';
import { SELECT_USER_STATE } from './user/user.selector';
import { User } from '../../core/user/user.interface';

@Injectable()
export class AppEffects {

  private actions$ = inject(Actions);
  private store$ = inject(Store);

  private userService$ = inject(UserService);
  private snackbarService$ = inject(SnackbarService);

  setTheme$ = createEffect(() => this.actions$.pipe(
      ofType(ACTION_SET_THEME),
      withLatestFrom(this.store$.select(SELECT_USER_STATE)),
      filter(([_, userState]) => !!userState.user),
      switchMap(([action, userState]) =>
        this.userService$.updateTheme(userState.user.id, {theme: action.theme}).pipe(
          mergeMap((user: User) => [
            ACTION_USER_SET({user}), ACTION_SHOW_SNACKBAR({message: 'Theme updated', icon: 'routine'})
          ])
        )
      )
    )
  );

  setLanguage$ = createEffect(() => this.actions$.pipe(
      ofType(ACTION_SET_LANGUAGE),
      withLatestFrom(this.store$.select(SELECT_USER_STATE)),
      filter(([_, userState]) => !!userState.user),
      switchMap(([action, userState]) =>
        this.userService$.updateLanguage(userState.user.id, {language: action.language}).pipe(
          mergeMap((user: User) => [
            ACTION_USER_SET({user}), ACTION_SHOW_SNACKBAR({message: 'Language updated', icon: 'language'})
          ])
        )
      )
    )
  );

  showSnackbar$ = createEffect(() => this.actions$.pipe(
      ofType(ACTION_SHOW_SNACKBAR),
      tap(({message, icon, duration}) => {
        this.snackbarService$.show(message, icon, duration);
      })
    ),
    {dispatch: false}
  );
}
