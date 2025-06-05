import { createAction, props } from '@ngrx/store';
import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { View } from '../../core/view/view.const';
import { Theme } from '../../core/theme/theme.const';
import { UserState } from './app.state';

export const ACTION_SET_VIEW = createAction(
  '[App] Set view', props<{ view: View }>()
);

export const ACTION_SET_THEME = createAction(
  '[App] Set theme', props<{ theme: Theme }>()
);

export const ACTION_SET_BREAKPOINT = createAction(
  '[App] Set breakpoint', props<{ breakpoint: Breakpoint }>()
);

export const ACTION_LOGIN = createAction(
  '[User] Login', props<{ user: UserState }>()
);

export const ACTION_LOGOUT = createAction(
  '[User] Logout'
);
