import { AsyncPipe, NgForOf, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { ACTION_SET_GAME_ACTIVE_CELL } from '../../../store/app/app.actions';
import { SELECT_BREAKPOINT } from '../../../store/app/app.selectors';
import { GameState } from '../../../store/app/app.state';
import { Game } from '../game.interface';
import { Cell } from './game-board.interface';

@Component({
  selector: 'soo-game-board',
  standalone: true,
  imports: [NgForOf, NgIf, AsyncPipe],
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

  activate(game: Game, row: number, col: number, value: number, activeCell?: Cell) {
    if (!this.isLocked(game, row, col)) {
      this.store.dispatch(ACTION_SET_GAME_ACTIVE_CELL({
        activeCell: activeCell?.row == row && activeCell?.col == col ? undefined : {row, col, value}
      }));
    }
  }

  // Conditioning methods

  isActive(game: Game, row: number, col: number, activeCell?: Cell): boolean {
    return !this.isLocked(game, row, col) && activeCell?.row == row && activeCell?.col == col;
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

  isLocked(game: Game, row: number, col: number): boolean {
    return game.locks.some(([r, c]) => r === row && c === col);
  }
}
