import { Cell } from '../../../modules/game/game-board/game-board.interface';
import { Game } from '../../../modules/game/game.interface';

export interface GameState {
  game?: Game;
  mode?: GameMode;
  focus?: Cell;
}

export enum GameMode {
  PLAY = 'play', SKETCH = 'sketch', VIEW = 'view'
}
