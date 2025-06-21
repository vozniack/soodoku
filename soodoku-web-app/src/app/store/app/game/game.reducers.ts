import { createReducer, on } from '@ngrx/store';
import { Cell } from '../../../modules/game/game-board/game-board.interface';
import { Game } from '../../../modules/game/game.interface';
import { ACTION_GAME_SET, ACTION_GAME_SET_FOCUS, ACTION_GAME_MODE } from './game.actions';
import { initialGameState } from './game.const';
import { GameMode, GameState } from './game.state';

export const _gameReducer = createReducer(initialGameState,
  on(ACTION_GAME_SET, (state: GameState, newState) => onSetGame(state, newState.game, newState.mode, newState.focus)),
  on(ACTION_GAME_SET_FOCUS, (state: GameState, newState) => onSetGameFocus(state, newState.focus)),
  on(ACTION_GAME_MODE, (state: GameState, newState) => onSetGameMode(state, newState.mode)),
);

function onSetGame(state: GameState, game: Game, mode: GameMode, focus?: Cell): GameState {
  return {
    ...state,
    game: game,
    mode: mode,
    focus: focus
  };
}

function onSetGameFocus(state: GameState, focus?: Cell): GameState {
  return {
    ...state,
    game: state.game!!,
    mode: state.mode!!,
    focus: focus
  };
}

function onSetGameMode(state: GameState, newState: GameMode): GameState {
  return {
    ...state,
    game: state.game!!,
    mode: newState,
    focus: state.focus,
  };
}
