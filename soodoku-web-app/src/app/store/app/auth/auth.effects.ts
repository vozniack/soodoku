import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { User } from '../../../core/user/user.interface';
import { UserService } from '../../../core/user/user.service';
import { View } from '../../../core/view/view.const';
import { ACTION_SET_VIEW } from '../app.actions';
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
      switchMap(() => this.store$.select(SELECT_GAME_STATE)),
      filter((state: GameState) => state.game?.userId !== undefined),
      map(() => ACTION_SET_VIEW({view: View.HOME}))
    )
  );

  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_AUTH_LOGIN),
      switchMap(() => this.userService.getLoggedUser()),
      map((user: User) => ACTION_USER_SET({user: user}))
    )
  );
}
