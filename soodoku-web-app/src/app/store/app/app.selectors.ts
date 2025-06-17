import { createFeatureSelector, createSelector } from '@ngrx/store';

export const SELECT_APP_STATE = createSelector(
  createFeatureSelector<any>('soodokuStorage'), state => state
);

export const SELECT_APP_VIEW = createSelector(
  SELECT_APP_STATE, state => state.view
);

export const SELECT_APP_THEME = createSelector(
  SELECT_APP_STATE, state => state.theme
);

export const SELECT_APP_LANGUAGE = createSelector(
  SELECT_APP_STATE, state => state.language
);

export const SELECT_APP_BREAKPOINT = createSelector(
  SELECT_APP_STATE, state => state.breakpoint
);
