import { createAction, props } from '@ngrx/store';
import { Cell } from '../../../modules/game/game-board/game-board.interface';
import { Game } from '../../../modules/game/game.interface';

export const ACTION_GAME_SET = createAction(
  '[Game] Set game', props<{ game: Game, focus?: Cell }>()
);

export const ACTION_GAME_SET_FOCUS = createAction(
  '[Game] Set game focused cell', props<{ focus?: Cell }>()
);

export const ACTION_GAME_MOVE = createAction(
  '[Game] Move', props<{ value: number }>()
);

export const ACTION_GAME_MOVE_REVERT = createAction(
  '[Game] Revert move'
);

export const ACTION_GAME_WIPE = createAction(
  '[Game] Wipe'
);

