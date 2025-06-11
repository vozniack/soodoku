import { createReducer, on } from '@ngrx/store';
import { ACTION_AUTH_LOGIN, ACTION_AUTH_LOGOUT } from './auth.actions';
import { initialAuthState } from './auth.const';
import { AuthState } from './auth.state';

export const _authReducer = createReducer(initialAuthState,
  on(ACTION_AUTH_LOGIN, (state, newState) => onAuthLogin(state, newState.token)),
  on(ACTION_AUTH_LOGOUT, () => onAuthLogout()),
);

function onAuthLogin(state: AuthState, token?: string) {
  return {
    ...state,
    token: token,
  };
}

function onAuthLogout() {
  return {};
}
