import { AsyncPipe, NgForOf, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { IconComponent } from '../../../shared/components/icon/icon.component';
import { SELECT_BREAKPOINT } from '../../../store/app/app.selectors';
import { ACTION_GAME_SET_FOCUS } from '../../../store/app/game/game.actions';
import { GameState } from '../../../store/app/game/game.state';
import { Game, Move } from '../game.interface';
import { Cell } from './game-board.interface';

@Component({
  selector: 'soo-game-board',
  standalone: true,
  imports: [NgForOf, NgIf, AsyncPipe, IconComponent],
  templateUrl: './game-board.component.html',
  styleUrl: './game-board.component.scss'
})
export class GameBoardComponent {

  @Input() gameState$!: Observable<GameState>;

  breakpoint!: Breakpoint;

  constructor(private store: Store) {
    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  // Action methods

  activate(game: Game, row: number, col: number, value: number, focus?: Cell) {
    if (!this.isHint(game, row, col) && !this.isLocked(game, row, col)) {
      this.store.dispatch(ACTION_GAME_SET_FOCUS({
        focus: focus?.row == row && focus?.col == col ? undefined : {row, col, value}
      }));
    }
  }

  // Conditioning methods

  isActive(game: Game, row: number, col: number, focus?: Cell): boolean {
    return !this.isLocked(game, row, col) && focus?.row == row && focus?.col == col;
  }

  isActiveRelated(row: number, col: number, active?: Cell): boolean {
    return active?.row == row || active?.col == col;
  }

  isConflict(game: Game, row: number, col: number): boolean {
    return game.conflicts.some(conflict =>
      conflict.cells.some(([r, c]) => r === row && c === col)
    );
  }

  isConflictRelated(game: Game, row: number, col: number): boolean {
    return game.conflicts.some(conflict => {
      switch (conflict.type) {
        case 'ROW':
          return row === conflict.index;

        case 'COL':
          return col === conflict.index;

        case 'BOX': {
          return Math.floor(row / 3) * 3 + Math.floor(col / 3) === conflict.index;
        }
      }
    });
  }

  isHint(game: Game, row: number, col: number): boolean {
    return game.moves.some((move: Move) => move.type === 'HINT' && move.row === row && move.col === col);
  }

  isLocked(game: Game, row: number, col: number): boolean {
    return game.locks.some(([r, c]) => r === row && c === col);
  }

  isCorrect(game: Game, row: number, col: number): boolean {
    return game.solved != undefined && !this.isLocked(game, row, col) && game.board[row][col] == game.solved[row][col];
  }

  isIncorrect(game: Game, row: number, col: number): boolean {
    return game.solved != undefined && game.board[row][col] != game.solved[row][col];
  }
}
