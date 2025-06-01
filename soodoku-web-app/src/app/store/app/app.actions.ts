import { createAction, props } from '@ngrx/store';
import { View } from '../../core/view/view.const';
import { Theme } from '../../core/theme/theme.const';
import { UserState } from './app.state';

export const ACTION_SET_VIEW = createAction(
  '[App][User] Set view', props<{ view: View }>()
);

export const ACTION_SET_THEME = createAction(
  '[App][User] Set theme', props<{ theme: Theme }>()
);
export const ACTION_LOGIN = createAction(
  '[App][User] Login', props<{ user: UserState }>()
);

export const ACTION_LOGOUT = createAction(
  '[App][User] Logout'
);
