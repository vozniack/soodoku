import { Game } from '../../../modules/game/game.interface';
import { ACTION_GAME_SET } from './game.actions';
import { GameMode, GameState } from './game.state';

export function buildSetGameAction(updatedGame: Game, gameState: GameState) {
  return ACTION_GAME_SET({
    game: updatedGame,
    mode: gameState.mode ?? GameMode.PLAY,
    focus: gameState.focus !== undefined ? {
      row: gameState.focus.row,
      col: gameState.focus.col,
      value: updatedGame.board[gameState.focus.row][gameState.focus.col]
    } : undefined
  });
}

export function noteValues(values: string[], value: number): string[] {
  const key = (p: string) => `${p}${value.toString()}`;
  const filtered = values.filter(v => v !== key('+') && v !== key('-'));

  return values.includes(key('+'))
    ? [...filtered, key('-')]
    : values.includes(key('-'))
      ? filtered
      : [...filtered, key('+')];
}
