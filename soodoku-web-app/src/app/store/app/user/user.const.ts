import { Theme } from '../../../core/theme/theme.const';
import { UserState } from './user.state';

export const initialUserState: UserState = {
  theme: Theme.LIGHT,
  user: undefined
}
