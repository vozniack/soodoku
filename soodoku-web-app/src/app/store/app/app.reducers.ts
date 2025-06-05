import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { Theme } from '../../core/theme/theme.const';
import { View } from '../../core/view/view.const';
import { SoodokuState, UserState } from './app.state';
import { initialAppState } from './app.const';
import { Action, createReducer, on } from '@ngrx/store';
import { ACTION_LOGIN, ACTION_LOGOUT, ACTION_SET_BREAKPOINT, ACTION_SET_THEME, ACTION_SET_VIEW, } from './app.actions';

export const initialState = initialAppState();

export function appReducer(state: SoodokuState = initialState, action: Action) {
  return _appReducer(state, action);
}

const _appReducer = createReducer(initialState,
  on(ACTION_SET_VIEW, (state, newState) => onSetView(state, newState.view)),
  on(ACTION_SET_THEME, (state, newState) => onSetTheme(state, newState.theme)),
  on(ACTION_SET_BREAKPOINT, (state, newState) => onSetBreakpoint(state, newState.breakpoint)),
  on(ACTION_LOGIN, (state, newState) => onSetUserState(state, newState.user)),
  on(ACTION_LOGOUT, (state) => onSetUserState(state, {}))
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

function onSetUserState(state: SoodokuState, user: UserState) {
  return {
    ...state,
    user: user
  };
}
