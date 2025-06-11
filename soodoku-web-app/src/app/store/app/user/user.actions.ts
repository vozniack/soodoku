import { createAction, props } from '@ngrx/store';
import { Theme } from '../../../core/theme/theme.const';
import { User } from '../../../core/user/user.interface';

export const ACTION_USER_SET = createAction(
  '[User] Set user', props<{ user: User }>()
);

export const ACTION_USER_SET_THEME = createAction(
  '[User] Set user theme', props<{ theme: Theme }>()
);
