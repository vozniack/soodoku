import { createSelector } from '@ngrx/store';
import { SELECT_APP_STATE } from '../app.selectors';

export const SELECT_USER_STATE = createSelector(
  SELECT_APP_STATE, state => state.userState
);
