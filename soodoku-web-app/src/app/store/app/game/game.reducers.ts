import { createReducer, on } from '@ngrx/store';
import { Cell } from '../../../modules/game/game-board/game-board.interface';
import { ACTION_GAME_SET, ACTION_GAME_SET_FOCUS } from './game.actions';
import { initialGameState } from './game.const';
import { GameState } from './game.state';

export const _gameReducer = createReducer(initialGameState,
  on(ACTION_GAME_SET, (state, newState: GameState) => onSetGame(state, newState)),
  on(ACTION_GAME_SET_FOCUS, (state, newState) => onSetGameFocus(state, newState.focus)),
);

function onSetGame(state: GameState, newState: GameState) {
  return {
    ...state,
    gameState: {
      game: newState.game,
      focus: newState.focus
    }
  };
}

function onSetGameFocus(state: GameState, focus?: Cell) {
  return {
    ...state,
    gameState: {
      game: state.game!!,
      focus: focus
    }
  };
}
