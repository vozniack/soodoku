import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { AvatarComponent } from '../../shared/components/avatar/avatar.component';
import { LogoComponent } from '../../shared/components/logo/logo.component';
import { fadeInAnimation } from '../../shared/animations/fade-in-animation';
import { SELECT_BREAKPOINT } from '../../store/app/app.selectors';
import { GameControlComponent } from './game-control/game-control.component';
import { GameInfoComponent } from './game-info/game-info.component';
import { GameBoardComponent } from './game-board/game-board.component';
import { Conflict } from './game-board/game-board.interface';
import { GameNumbersComponent } from './game-numbers/game-numbers.component';

@Component({
  selector: 'soo-game',
  standalone: true,
  imports: [GameInfoComponent, GameBoardComponent, GameControlComponent, GameNumbersComponent, LogoComponent, AvatarComponent, NgIf],
  templateUrl: './game.component.html',
  styleUrl: './game.component.scss',
  animations: [fadeInAnimation]
})
export class GameComponent {

  breakpoint!: Breakpoint;

  board: number[][] = [
    [5, 7, 0, 6, 7, 0, 9, 1, 2],
    [6, 0, 2, 1, 9, 8, 0, 4, 8],
    [1, 9, 8, 3, 4, 2, 5, 6, 0],
    [8, 5, 9, 7, 6, 1, 0, 2, 3],
    [4, 0, 6, 8, 0, 8, 0, 9, 1],
    [7, 1, 3, 9, 2, 0, 0, 5, 6],
    [0, 6, 1, 5, 3, 7, 2, 8, 4],
    [2, 8, 7, 0, 1, 9, 6, 3, 5],
    [3, 4, 5, 2, 8, 6, 1, 7, 0]
  ];

  locks: [number, number][] = [];

  conflicts: Conflict[] = [
    {
      type: 'ROW',
      value: 7,
      index: 0,
      cells: [{row: 0, col: 1}, {row: 0, col: 4}]
    },
    {
      type: 'COL',
      value: 8,
      index: 0,
      cells: [{row: 1, col: 5}, {row: 4, col: 5}]
    },
  ];

  constructor(private store: Store) {
    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();

    this.locks = this.board.flatMap((row, rowIndex) =>
      row.map((value, colIndex) =>
        value !== 0 && Math.random() < 0.5 ? [rowIndex, colIndex] as [number, number] : null
      ).filter(Boolean) as [number, number][]
    );
  }
}
