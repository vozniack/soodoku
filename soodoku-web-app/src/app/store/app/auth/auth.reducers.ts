import { createReducer, on } from '@ngrx/store';
import { ACTION_AUTH_LOGIN, ACTION_AUTH_LOGOUT, ACTION_AUTH_REFRESH } from './auth.actions';
import { initialAuthState } from './auth.const';
import { AuthState } from './auth.state';

export const _authReducer = createReducer(initialAuthState,
  on(ACTION_AUTH_LOGIN, (_, newState) => onAuthLoginOrRefresh(newState.accessToken, newState.refreshToken)),
  on(ACTION_AUTH_REFRESH, (_, newState) => onAuthLoginOrRefresh(newState.accessToken, newState.refreshToken)),
  on(ACTION_AUTH_LOGOUT, () => onAuthLogout()),
);

function onAuthLoginOrRefresh(accessToken?: string, refreshToken?: string): AuthState {
  return {accessToken, refreshToken,};
}

function onAuthLogout() {
  return {};
}
