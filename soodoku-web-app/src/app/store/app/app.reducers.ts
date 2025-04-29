import { SoodokuState, UserState } from './app.state';
import { initialAppState } from './app.const';
import { Action, createReducer, on } from '@ngrx/store';
import { ACTION_LOGIN, ACTION_LOGOUT, ACTION_SET_THEME, } from './app.actions';

export const initialState = initialAppState();

export function appReducer(state: SoodokuState = initialState, action: Action) {
  return _appReducer(state, action);
}

const _appReducer = createReducer(initialState,
  on(ACTION_LOGIN, (state, newState) => onSetUserState(state, newState.user)),
  on(ACTION_LOGOUT, (state) => onSetUserState(state, {})),
  on(ACTION_SET_THEME, (state, newState) => onSetTheme(state, newState.theme)),
);

function onSetUserState(state: SoodokuState, user: UserState) {
  return {
    ...state,
    user: user
  };
}

function onSetTheme(state: SoodokuState, theme: string) {
  return {
    ...state,
    theme: theme
  };
}
