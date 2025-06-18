import { createAction, props } from '@ngrx/store';

export const ACTION_AUTH_LOGIN = createAction(
  '[Auth] Login', props<{ accessToken?: string, refreshToken?: string }>()
);

export const ACTION_AUTH_REFRESH = createAction(
  '[Auth] Refresh', props<{ accessToken?: string, refreshToken?: string }>()
);

export const ACTION_AUTH_LOGOUT = createAction(
  '[Auth] Logout'
);
