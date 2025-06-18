import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Action, select, Store } from '@ngrx/store';
import { filter, mergeMap, take } from 'rxjs';
import { map, switchMap, tap, withLatestFrom } from 'rxjs/operators';
import { mapLanguage } from '../../../core/language/language.function';
import { mapTheme } from '../../../core/theme/theme.functions';
import { UserService } from '../../../core/user/user.service';
import { View } from '../../../core/view/view.const';
import { ACTION_SET_LANGUAGE, ACTION_SET_THEME, ACTION_SET_VIEW, ACTION_SHOW_SNACKBAR } from '../app.actions';
import { SELECT_APP_STATE } from '../app.selectors';
import { SELECT_GAME_STATE } from '../game/game.selectors';
import { GameState } from '../game/game.state';
import { ACTION_USER_SET } from '../user/user.actions';
import { ACTION_AUTH_LOGIN, ACTION_AUTH_LOGOUT } from './auth.actions';
import { SELECT_AUTH_STATE } from './auth.selectors';
import { AuthState } from './auth.state';

@Injectable()
export class AuthEffects {

  private actions$ = inject(Actions);
  private store$ = inject(Store);

  private userService = inject(UserService);

  logout$ = createEffect(() => this.actions$.pipe(
      ofType(ACTION_AUTH_LOGOUT),
      tap(() => this.store$.dispatch(ACTION_USER_SET({user: undefined}))),
      withLatestFrom(this.store$.select(SELECT_GAME_STATE)),
      filter(([_, state]: [any, GameState]) => state.game?.userId !== undefined),
      tap(() => this.store$.dispatch(ACTION_SHOW_SNACKBAR({message: 'Good bye!', icon: 'waving_hand'}))),
      map(() => ACTION_SET_VIEW({view: View.HOME}))
    )
  );

  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_AUTH_LOGIN),
      switchMap(() =>
        this.store$.pipe(
          select(SELECT_AUTH_STATE),
          map((authState: AuthState) => authState.accessToken),
          filter((token): token is string => !!token),
          take(1),
          switchMap(() => this.userService.getLoggedUser()),
          withLatestFrom(this.store$.select(SELECT_APP_STATE)),
          map(([user, appState]) => {
            const actions: Action[] = [ACTION_USER_SET({user})];

            const language = mapLanguage(user.language);
            if (appState.language !== user.language) {
              actions.push(ACTION_SET_LANGUAGE({language}));
            }

            const theme = mapTheme(user.theme);
            if (appState.theme !== theme) {
              actions.push(ACTION_SET_THEME({theme}));
            }

            return actions;
          }),
          mergeMap(actions => actions)
        )
      )
    )
  );
}
