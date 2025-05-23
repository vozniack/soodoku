import { createFeatureSelector, createSelector } from '@ngrx/store';

export const SELECT_APP_STATE = createSelector(
  createFeatureSelector<any>('soodoku-storage'), state => state
);

export const SELECT_USER_STATE = createSelector(
  SELECT_APP_STATE, state => state.user
);

export const SELECT_THEME = createSelector(
  SELECT_APP_STATE, state => state.theme
);
