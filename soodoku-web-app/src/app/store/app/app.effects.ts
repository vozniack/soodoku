import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { User } from '../../core/user/user.interface';
import { UserService } from '../../core/user/user.service';
import { View } from '../../core/view/view.const';
import { ACTION_LOGIN, ACTION_LOGOUT, ACTION_SET_USER, ACTION_SET_VIEW, } from './app.actions';
import { SELECT_GAME_STATE } from './app.selectors';
import { GameState } from './app.state';

@Injectable()
export class AppEffects {

  private actions$ = inject(Actions);
  private store$ = inject(Store);
  private userService = inject(UserService);

  logout$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_LOGOUT),
      switchMap(() => this.store$.select(SELECT_GAME_STATE)),
      filter((state: GameState) => state.game?.userId !== undefined),
      map(() => ACTION_SET_VIEW({view: View.HOME}))
    )
  );

  login$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_LOGIN),
      switchMap(() => this.userService.getLoggedUser()),
      map((user: User) => ACTION_SET_USER({user: user}))
    )
  );
}
