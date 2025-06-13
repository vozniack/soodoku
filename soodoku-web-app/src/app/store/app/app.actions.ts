import { createAction, props } from '@ngrx/store';
import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { View } from '../../core/view/view.const';

export const ACTION_SET_VIEW = createAction(
  '[App] Set view', props<{ view: View }>()
);

export const ACTION_SET_BREAKPOINT = createAction(
  '[App] Set breakpoint', props<{ breakpoint: Breakpoint }>()
);

export const ACTION_SHOW_SNACKBAR = createAction(
  '[App] Show snackbar', props<{ message: string; icon?: string, duration?: number }>()
);
