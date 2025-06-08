import { ACTION_SET_GAME } from '../../store/app/app.actions';
import { Cell } from './game-board/game-board.interface';
import { Game } from './game.interface';

export function setGameActionBuilder(updatedGame: Game, activeCell?: Cell) {
  return ACTION_SET_GAME({
    game: updatedGame,
    activeCell: activeCell !== undefined ? {
      ...activeCell,
      value: updatedGame.board[activeCell.row][activeCell.col]
    } : undefined
  });
}
