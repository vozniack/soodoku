import { Game } from '../../../modules/game/game.interface';
import { ACTION_GAME_SET } from './game.actions';
import { GameState } from './game.state';

export function buildSetGameAction(updatedGame: Game, gameState: GameState) {
  return ACTION_GAME_SET({
    game: updatedGame,
    focus: {
      row: gameState.focus!.row,
      col: gameState.focus!.col,
      value: updatedGame.board[gameState.focus!.row][gameState.focus!.col]
    }
  });
}
