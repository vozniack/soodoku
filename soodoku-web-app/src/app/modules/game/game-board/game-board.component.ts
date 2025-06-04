import { NgForOf } from '@angular/common';
import { Component, HostListener, Input, OnInit } from '@angular/core';
import { Cell, Conflict } from './game-board.interface';

@Component({
  selector: 'soo-game-board',
  standalone: true,
  imports: [NgForOf],
  templateUrl: './game-board.component.html',
  styleUrl: './game-board.component.scss'
})
export class GameBoardComponent implements OnInit {

  @Input() board: number[][] = [];
  @Input() locks: [number, number][] = [];
  @Input() conflicts: Conflict[] = [];

  active?: Cell;
  cellSize: number = 64;

  ngOnInit(): void {
    this.calculateCellSize();
  }

  @HostListener('window:resize')
  resize(): void {
    this.calculateCellSize();
  }

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

  // Supporting methods

  private calculateCellSize(): void {
    const width = (window.innerWidth < 1000 ? window.innerWidth : 1000) - 100;
    const height = window.innerHeight - 356;

    this.cellSize = height > width ? width / 9 : height / 9;
  }
}
