import { View } from '../../core/view/view.const';
import { Theme } from '../../core/theme/theme.const';
import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { Cell } from '../../modules/game/game-board/game-board.interface';
import { Game } from '../../modules/game/game.interface';

export interface SoodokuState {
  view: View;
  theme: Theme;
  breakpoint: Breakpoint,

  userState: UserState;
  gameState?: GameState;
}

export interface UserState {
  token?: string;
}

export interface GameState {
  game: Game,
  activeCell?: Cell,
}
