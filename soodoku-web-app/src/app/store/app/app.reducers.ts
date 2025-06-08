import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { Theme } from '../../core/theme/theme.const';
import { View } from '../../core/view/view.const';
import { Cell } from '../../modules/game/game-board/game-board.interface';
import { SoodokuState, UserState } from './app.state';
import { initialAppState } from './app.const';
import { Action, createReducer, on } from '@ngrx/store';
import {
  ACTION_LOGIN,
  ACTION_LOGOUT,
  ACTION_SET_BREAKPOINT,
  ACTION_SET_THEME,
  ACTION_SET_VIEW, ACTION_SET_GAME_ACTIVE_CELL, ACTION_SET_GAME,
} from './app.actions';
import { Game } from '../../modules/game/game.interface';

export const initialState = initialAppState();

export function appReducer(state: SoodokuState = initialState, action: Action) {
  return _appReducer(state, action);
}

const _appReducer = createReducer(initialState,
  on(ACTION_SET_VIEW, (state, newState) => onSetView(state, newState.view)),
  on(ACTION_SET_THEME, (state, newState) => onSetTheme(state, newState.theme)),
  on(ACTION_SET_BREAKPOINT, (state, newState) => onSetBreakpoint(state, newState.breakpoint)),
  on(ACTION_LOGIN, (state, newState) => onSetUserState(state, newState.userState)),
  on(ACTION_LOGOUT, (state) => onSetUserState(state, {})),
  on(ACTION_SET_GAME, (state, newState) => onSetGame(state, newState.game, newState.activeCell)),
  on(ACTION_SET_GAME_ACTIVE_CELL, (state, newState) => onSetGameActiveCell(state, newState.activeCell)),
);

function onSetView(state: SoodokuState, view: View) {
  return {
    ...state,
    view: view
  };
}

function onSetTheme(state: SoodokuState, theme: Theme) {
  return {
    ...state,
    theme: theme
  };
}

function onSetBreakpoint(state: SoodokuState, breakpoint: Breakpoint) {
  return {
    ...state,
    breakpoint: breakpoint
  };
}

function onSetUserState(state: SoodokuState, userState: UserState) {
  return {
    ...state,
    userState: userState
  };
}

function onSetGame(state: SoodokuState, game: Game, activeCell?: Cell) {
  return {
    ...state,
    gameState: {
      game: game,
      activeCell: activeCell
    }
  };
}

function onSetGameActiveCell(state: SoodokuState, activeCell?: Cell) {
  return {
    ...state,
    gameState: {
      game: state.gameState?.game!!,
      activeCell: activeCell
    }
  };
}
