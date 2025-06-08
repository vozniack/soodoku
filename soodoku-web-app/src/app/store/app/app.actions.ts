import { createAction, props } from '@ngrx/store';
import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { View } from '../../core/view/view.const';
import { Theme } from '../../core/theme/theme.const';
import { Cell } from '../../modules/game/game-board/game-board.interface';
import { Game } from '../../modules/game/game.interface';
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
  '[User] Login', props<{ userState: UserState }>()
);

export const ACTION_LOGOUT = createAction(
  '[User] Logout'
);

export const ACTION_SET_GAME = createAction(
  '[Game] Set game', props<{ game: Game, activeCell?: Cell }>()
);

export const ACTION_SET_GAME_ACTIVE_CELL = createAction(
  '[Game] Set game active cell', props<{ activeCell?: Cell }>()
);
