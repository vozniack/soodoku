import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { filter, mergeMap } from 'rxjs';
import { map, switchMap, tap, withLatestFrom } from 'rxjs/operators';
import { mapLanguage } from '../../../core/language/language.function';
import { mapTheme } from '../../../core/theme/theme.functions';
import { User } from '../../../core/user/user.interface';
import { UserService } from '../../../core/user/user.service';
import { View } from '../../../core/view/view.const';
import { ACTION_SET_LANGUAGE, ACTION_SET_THEME, ACTION_SET_VIEW, ACTION_SHOW_SNACKBAR } from '../app.actions';
import { SELECT_GAME_STATE } from '../game/game.selectors';
import { GameState } from '../game/game.state';
import { ACTION_USER_SET } from '../user/user.actions';
import { ACTION_AUTH_LOGIN, ACTION_AUTH_LOGOUT } from './auth.actions';

@Injectable()
export class AuthEffects {

  private actions$ = inject(Actions);
  private store$ = inject(Store);

  private userService = inject(UserService);

  logout$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_AUTH_LOGOUT),
      tap(() => this.store$.dispatch(ACTION_USER_SET({user: undefined}))),
      withLatestFrom(this.store$.select(SELECT_GAME_STATE)),
      filter(([_, state]: [any, GameState]) => state.game?.userId !== undefined),
      tap(() => this.store$.dispatch(ACTION_SHOW_SNACKBAR({message: 'You have been logged out', icon: 'waving_hand'}))),
      map(() => ACTION_SET_VIEW({view: View.HOME}))
    )
  );

  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_AUTH_LOGIN),
      switchMap(() => this.userService.getLoggedUser()),
      mergeMap((user: User) => [
        ACTION_USER_SET({user}),
        ACTION_SET_LANGUAGE({language: mapLanguage(user.language)}),
        ACTION_SET_THEME({theme: mapTheme(user.theme)})
      ])
    )
  );
}
