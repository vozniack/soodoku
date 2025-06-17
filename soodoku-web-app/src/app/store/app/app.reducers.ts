import { initialBreakpointState, initialLanguageState, initialThemeState, initialViewState } from './app.const';
import { combineReducers, createReducer, on } from '@ngrx/store';
import { ACTION_SET_BREAKPOINT, ACTION_SET_LANGUAGE, ACTION_SET_THEME, ACTION_SET_VIEW, } from './app.actions';
import { AppState } from './app.state';
import { _authReducer } from './auth/auth.reducers';
import { _gameReducer } from './game/game.reducers';
import { _userReducer } from './user/user.reducers';

const _viewReducer = createReducer(initialViewState,
  on(ACTION_SET_VIEW, (_, newState) => newState.view),
);

const _themeReducer = createReducer(initialThemeState,
  on(ACTION_SET_THEME, (_, newState) => newState.theme),
);

const _languageReducer = createReducer(initialLanguageState,
  on(ACTION_SET_LANGUAGE, (_, newState) => newState.language),
);

const _breakpointReducer = createReducer(initialBreakpointState,
  on(ACTION_SET_BREAKPOINT, (_, newState) => newState.breakpoint)
);

export const appReducers = combineReducers<AppState>({
  view: _viewReducer,
  theme: _themeReducer,
  language: _languageReducer,
  breakpoint: _breakpointReducer,

  authState: _authReducer,
  userState: _userReducer,
  gameState: _gameReducer,
});
