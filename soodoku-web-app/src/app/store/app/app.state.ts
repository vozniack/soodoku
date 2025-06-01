import { View } from '../../core/view/view.const';
import { Theme } from '../../core/theme/theme.const';

export interface SoodokuState {
  view: View;
  theme: Theme;
  user: UserState;
}

export interface UserState {
  token?: string;
}
