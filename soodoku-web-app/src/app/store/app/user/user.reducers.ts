import { createReducer, on } from '@ngrx/store';
import { Theme } from '../../../core/theme/theme.const';
import { User } from '../../../core/user/user.interface';
import { ACTION_USER_SET, ACTION_USER_SET_THEME } from './user.actions';
import { initialUserState } from './user.const';
import { UserState } from './user.state';

export const _userReducer = createReducer(initialUserState,
  on(ACTION_USER_SET, (state, newState) => onSetUser(state, newState.user)),
  on(ACTION_USER_SET_THEME, (state, newState) => onSetUserTheme(state, newState.theme)),
);

function onSetUser(state: UserState, user: User) {
  return {
    ...state,
    userState: {
      user: user
    }
  };
}

function onSetUserTheme(state: UserState, theme: Theme) {
  return {
    ...state,
    theme: theme
  };
}
