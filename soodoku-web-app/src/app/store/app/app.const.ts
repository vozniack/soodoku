import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { Theme } from '../../core/theme/theme.const';
import { View } from '../../core/view/view.const';
import { SoodokuState } from './app.state';

export const initialAppState = (): SoodokuState => {
  return {
    view: View.HOME,
    theme: Theme.LIGHT,
    breakpoint: Breakpoint.XL,
    user: {
      token: undefined
    }
  };
};
