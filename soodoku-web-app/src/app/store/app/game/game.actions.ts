import { createAction, props } from '@ngrx/store';
import { Cell } from '../../../modules/game/game-board/game-board.interface';
import { Game } from '../../../modules/game/game.interface';
import { GameMode } from './game.state';

export const ACTION_GAME_SET = createAction(
  '[Game] Set game', props<{ game: Game, mode: GameMode, focus?: Cell }>()
);

export const ACTION_GAME_MODE = createAction(
  '[Game] Set game mode', props<{ mode: GameMode }>()
);

export const ACTION_GAME_SET_FOCUS = createAction(
  '[Game] Set game focused cell', props<{ focus?: Cell }>()
);

export const ACTION_GAME_NEW = createAction(
  '[Game] Create new game', props<{ difficulty: string }>()
);

export const ACTION_GAME_MOVE = createAction(
  '[Game] Move (fill cell)', props<{ value: number }>()
);

export const ACTION_GAME_REVERT = createAction(
  '[Game] Revert move'
);

export const ACTION_GAME_WIPE = createAction(
  '[Game] Wipe cell'
);

export const ACTION_GAME_NOTE = createAction(
  '[Game] Change note', props<{ value?: number }>()
);

export const ACTION_GAME_NOTES_WIPE = createAction(
  '[Game] Wipe all notes'
);

export const ACTION_GAME_HINT = createAction(
  '[Game] Use hint'
);

export const ACTION_GAME_SURRENDER = createAction(
  '[Game] Surrender game'
);

export const ACTION_GAME_END = createAction(
  '[Game] End game'
);
