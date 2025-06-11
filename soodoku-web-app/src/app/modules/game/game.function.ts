import { ACTION_GAME_SET } from '../../store/app/game/game.actions';
import { Cell } from './game-board/game-board.interface';
import { Game } from './game.interface';

export function setGameActionBuilder(updatedGame: Game, focus?: Cell) {
  return ACTION_GAME_SET({
    game: updatedGame,
    focus: focus !== undefined ? {
      ...focus,
      value: updatedGame.board[focus.row][focus.col]
    } : undefined
  });
}
