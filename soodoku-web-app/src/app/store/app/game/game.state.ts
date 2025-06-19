import { Cell } from '../../../modules/game/game-board/game-board.interface';
import { Game } from '../../../modules/game/game.interface';

export interface GameState {
  game?: Game;
  focus?: Cell;

  sketch: boolean;
}
