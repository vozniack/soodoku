import { View } from '../../core/view/view.const';
import { Theme } from '../../core/theme/theme.const';
import { SoodokuState } from './app.state';

export const initialAppState = (): SoodokuState => {
  return {
    view: View.HOME,
    theme: Theme.LIGHT,
    user: {
      token: undefined
    }
  };
};
