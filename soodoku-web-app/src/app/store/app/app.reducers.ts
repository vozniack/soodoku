import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { View } from '../../core/view/view.const';
import { initialBreakpointState, initialViewState } from './app.const';
import { combineReducers, createReducer, on } from '@ngrx/store';
import { ACTION_SET_BREAKPOINT, ACTION_SET_VIEW, } from './app.actions';
import { AppState } from './app.state';
import { _authReducer } from './auth/auth.reducers';
import { _gameReducer } from './game/game.reducers';
import { _userReducer } from './user/user.reducers';

const _viewReducer = createReducer(initialViewState,
  on(ACTION_SET_VIEW, (_, newState) => onSetView(newState.view)),
);

const _breakpointReducer = createReducer(initialBreakpointState,
  on(ACTION_SET_BREAKPOINT, (_, newState) => onSetBreakpoint(newState.breakpoint))
);

function onSetView(view: View) {
  return view;
}

function onSetBreakpoint(breakpoint: Breakpoint) {
  return breakpoint;
}

export const appReducers = combineReducers<AppState>({
  view: _viewReducer,
  breakpoint: _breakpointReducer,

  authState: _authReducer,
  userState: _userReducer,
  gameState: _gameReducer,
});
