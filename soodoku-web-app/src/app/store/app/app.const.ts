import { SoodokuState } from './app.state';

export const initialAppState = (): SoodokuState => {
  return {
    theme: 'theme-light',
    user: {
      token: undefined
    }
  };
};
