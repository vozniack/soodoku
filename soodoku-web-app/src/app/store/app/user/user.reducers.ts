import { createReducer, on } from '@ngrx/store';
import { User } from '../../../core/user/user.interface';
import { ACTION_USER_SET } from './user.actions';
import { initialUserState } from './user.const';
import { UserState } from './user.state';

export const _userReducer = createReducer(initialUserState,
  on(ACTION_USER_SET, (state, newState) => onSetUser(state, newState.user)),
);

function onSetUser(state: UserState, user?: User) {
  return {
    ...state,
    user: user
  };
}
