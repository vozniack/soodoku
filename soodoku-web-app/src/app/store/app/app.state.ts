import { View } from '../../core/view/view.const';
import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { AuthState } from './auth/auth.state';
import { GameState } from './game/game.state';
import { UserState } from './user/user.state';

export interface AppState {
  view: View;
  breakpoint: Breakpoint,

  authState: AuthState;
  userState: UserState;
  gameState: GameState;
}
