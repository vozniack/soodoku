import { NgForOf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Cell, Conflict } from './game-board.interface';

@Component({
  selector: 'soo-game-board',
  standalone: true,
  imports: [NgForOf],
  templateUrl: './game-board.component.html',
  styleUrl: './game-board.component.scss'
})
export class GameBoardComponent {

  @Input() board: number[][] = [];
  @Input() locks: [number, number][] = [];
  @Input() conflicts: Conflict[] = [];

  active?: Cell;

  // Action methods

  activate(row: number, col: number) {
    if (!this.isLocked(row, col)) {
      this.active = this.active?.row == row && this.active?.col == col ? undefined : {row, col};
    }
  }

  // Conditioning methods

  isActive(row: number, col: number): boolean {
    return !this.isLocked(row, col) && this.active?.row == row && this.active?.col == col;
  }

  isActiveRelated(row: number, col: number): boolean {
    return this.active?.row == row || this.active?.col == col;
  }

  isConflictRelated(row: number, col: number): boolean {
    return this.conflicts.some(conflict =>
      conflict.cells.some((cell: Cell) =>
        conflict.type === 'ROW' ? cell.row === row : cell.col === col)
    );
  }

  isConflict(row: number, col: number): boolean {
    return this.conflicts.some(conflict =>
      conflict.cells.some((cell: Cell) => cell.row === row && cell.col === col)
    );
  }

  isLocked(row: number, col: number): boolean {
    return this.locks.some(([r, c]) => r === row && c === col);
  }
}
