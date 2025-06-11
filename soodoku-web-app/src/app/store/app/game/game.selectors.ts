import { createSelector } from '@ngrx/store';
import { SELECT_APP_STATE } from '../app.selectors';

export const SELECT_GAME_STATE = createSelector(
  SELECT_APP_STATE, state => state.gameState
);

export const SELECT_GAME_FOCUS = createSelector(
  SELECT_GAME_STATE, state => state.focus
);
