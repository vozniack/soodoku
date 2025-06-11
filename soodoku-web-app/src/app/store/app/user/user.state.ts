import { Theme } from '../../../core/theme/theme.const';
import { User } from '../../../core/user/user.interface';

export interface UserState {
  theme: Theme;
  user?: User;
}
