import { View } from '../../core/view/view.const';
import { Theme } from '../../core/theme/theme.const';
import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';

export interface SoodokuState {
  view: View;
  theme: Theme;
  breakpoint: Breakpoint,
  user: UserState;
}

export interface UserState {
  token?: string;
}
