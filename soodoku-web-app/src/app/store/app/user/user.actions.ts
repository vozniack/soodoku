import { createAction, props } from '@ngrx/store';
import { User } from '../../../core/user/user.interface';

export const ACTION_USER_SET = createAction(
  '[User] Set user', props<{ user?: User }>()
);
