import { createFeatureSelector, createSelector } from '@ngrx/store';

export const SELECT_APP_STATE = createSelector(
  createFeatureSelector<any>('soodokuStorage'), state => state
);

export const SELECT_VIEW = createSelector(
  SELECT_APP_STATE, state => state.view
);

export const SELECT_BREAKPOINT = createSelector(
  SELECT_APP_STATE, state => state.breakpoint
);
