import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { concatMap, filter } from 'rxjs';
import { map, switchMap, tap, withLatestFrom } from 'rxjs/operators';
import { DialogService } from '../../../core/dialog/dialog.service';
import { View } from '../../../core/view/view.const';
import { Game } from '../../../modules/game/game.interface';
import { GameService } from '../../../modules/game/game.service';
import { GameEndDialogComponent } from '../../../shared/dialogs/game-end-dialog/game-end-dialog.component';
import { ACTION_SET_VIEW } from '../app.actions';
import {
  ACTION_GAME_SURRENDER,
  ACTION_GAME_MOVE,
  ACTION_GAME_NEW,
  ACTION_GAME_REVERT,
  ACTION_GAME_SET,
  ACTION_GAME_WIPE,
  ACTION_GAME_END,
  ACTION_GAME_HINT
} from './game.actions';
import { buildSetGameAction } from './game.function';
import { SELECT_GAME_STATE } from './game.selectors';

@Injectable()
export class GameEffects {

  private actions$ = inject(Actions);
  private store$ = inject(Store);

  private gameService$ = inject(GameService);
  private dialogService$ = inject(DialogService);

  new$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_GAME_NEW),
      concatMap(({difficulty}) =>
        this.gameService$.new(difficulty).pipe(
          switchMap(game => [
            ACTION_GAME_SET({game}),
            ACTION_SET_VIEW({view: View.GAME}),
          ])
        )
      )
    )
  );

  move$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_GAME_MOVE),
      withLatestFrom(this.store$.select(SELECT_GAME_STATE)),
      filter(([_, {game, focus}]) => !!game && !!focus),
      switchMap(([action, gameState]) =>
        this.gameService$.move(gameState.game!.id, gameState.focus!.row, gameState.focus!.col, action.value).pipe(
          map((updatedGame: Game) => buildSetGameAction(updatedGame, gameState))
        )
      )
    )
  );

  revert$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_GAME_REVERT),
      withLatestFrom(this.store$.select(SELECT_GAME_STATE)),
      filter(([_, {game, focus}]) => !!game && !!focus),
      switchMap(([_, gameState]) =>
        this.gameService$.revert(gameState.game!.id).pipe(
          map((updatedGame: Game) => buildSetGameAction(updatedGame, gameState))
        )
      )
    )
  );

  wipe$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_GAME_WIPE),
      withLatestFrom(this.store$.select(SELECT_GAME_STATE)),
      filter(([_, {game, focus}]) => !!game && !!focus),
      switchMap(([_, gameState]) =>
        this.gameService$.move(gameState.game!.id, gameState.focus!.row, gameState.focus!.col, 0).pipe(
          map((updatedGame: Game) => buildSetGameAction(updatedGame, gameState))
        )
      )
    )
  );

  hint$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_GAME_HINT),
      withLatestFrom(this.store$.select(SELECT_GAME_STATE)),
      filter(([_, gameState]) => !!gameState.game),
      switchMap(([_, gameState]) =>
        this.gameService$.hint(gameState.game!.id).pipe(
          map((updatedGame: Game) => buildSetGameAction(updatedGame, gameState))
        )
      )
    )
  );

  surrender$ = createEffect(() =>
    this.actions$.pipe(
      ofType(ACTION_GAME_SURRENDER),
      withLatestFrom(this.store$.select(SELECT_GAME_STATE)),
      filter(([_, {game}]) => !!game),
      switchMap(([_, gameState]) =>
        this.gameService$.end(gameState.game!.id).pipe(
          map((updatedGame: Game) => ACTION_GAME_END({game: updatedGame}))
        )
      )
    )
  );

  end$ = createEffect(() =>
      this.actions$.pipe(
        ofType(ACTION_GAME_END),
        tap(() => this.dialogService$.open(GameEndDialogComponent, {closing: false})),
        tap(() => this.store$.dispatch(ACTION_SET_VIEW({view: View.HOME})))
      ),
    {dispatch: false}
  );
}
