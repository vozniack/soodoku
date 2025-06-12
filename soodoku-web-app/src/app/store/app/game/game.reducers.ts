import { createReducer, on } from '@ngrx/store';
import { Cell } from '../../../modules/game/game-board/game-board.interface';
import { Game } from '../../../modules/game/game.interface';
import { ACTION_GAME_END, ACTION_GAME_SET, ACTION_GAME_SET_FOCUS } from './game.actions';
import { initialGameState } from './game.const';
import { GameState } from './game.state';

export const _gameReducer = createReducer(initialGameState,
  on(ACTION_GAME_SET, (state: GameState, newState: GameState) => onSetGame(state, newState)),
  on(ACTION_GAME_SET_FOCUS, (state: GameState, newState: GameState) => onSetGameFocus(state, newState.focus)),
  on(ACTION_GAME_END, (state: GameState, newState: GameState) => onEndGame(state, newState.game!!)),
);

function onSetGame(state: GameState, newState: GameState) {
  return {
    ...state,
    game: newState.game,
    focus: newState.focus
  };
}

function onSetGameFocus(state: GameState, focus?: Cell) {
  return {
    ...state,
    game: state.game!!,
    focus: focus
  };
}

function onEndGame(state: GameState, game: Game) {
  return {
    ...state,
    game: game
  };
}


