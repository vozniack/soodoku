import { createReducer, on } from '@ngrx/store';
import { Cell } from '../../../modules/game/game-board/game-board.interface';
import { Game } from '../../../modules/game/game.interface';
import { ACTION_GAME_SET, ACTION_GAME_SET_FOCUS, ACTION_GAME_SKETCH } from './game.actions';
import { initialGameState } from './game.const';
import { GameState } from './game.state';

export const _gameReducer = createReducer(initialGameState,
  on(ACTION_GAME_SET, (state: GameState, newState) => onSetGame(state, newState.game, newState.focus, newState.sketch)),
  on(ACTION_GAME_SET_FOCUS, (state: GameState, newState) => onSetGameFocus(state, newState.focus)),
  on(ACTION_GAME_SKETCH, (state: GameState, _) => onSetGameSketch(state)),
);

function onSetGame(state: GameState, game: Game, focus?: Cell, sketch?: boolean): GameState {
  return {
    ...state,
    game: game,
    focus: focus,
    sketch: sketch ?? state.sketch
  };
}

function onSetGameFocus(state: GameState, focus?: Cell): GameState {
  return {
    ...state,
    game: state.game!!,
    focus: focus,
    sketch: state.sketch!!
  };
}

function onSetGameSketch(state: GameState): GameState {
  return {
    ...state,
    game: state.game!!,
    focus: state.focus,
    sketch: !state.sketch,
  };
}
