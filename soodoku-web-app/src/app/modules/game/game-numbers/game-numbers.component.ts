import { AsyncPipe, NgForOf, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { SELECT_APP_BREAKPOINT } from '../../../store/app/app.selectors';
import { ACTION_GAME_MOVE, ACTION_GAME_NOTE } from '../../../store/app/game/game.actions';
import { GameMode, GameState } from '../../../store/app/game/game.state';
import { Cell } from '../game-board/game-board.interface';
import { Note } from '../game.interface';

@Component({
  selector: 'soo-game-numbers',
  standalone: true,
  imports: [NgForOf, AsyncPipe, NgIf],
  templateUrl: './game-numbers.component.html',
  styleUrl: './game-numbers.component.scss'
})
export class GameNumbersComponent {

  GameMode = GameMode;

  @Input() gameState$!: Observable<GameState>;

  breakpoint!: Breakpoint;

  numbers: number[] = [...Array(9).keys()].map(i => i + 1);

  constructor(private store: Store) {
    this.store.select(SELECT_APP_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  move(value: number): void {
    this.store.dispatch(ACTION_GAME_MOVE({value}));
  }

  note(value: number): void {
    this.store.dispatch(ACTION_GAME_NOTE({value}));
  }

  hasNotes(value: number, sketch: boolean, sign: string, notes: Note[], focus?: Cell): boolean {
    if (!focus || !sketch) {
      return false;
    }

    const note = notes.find(n => n.row === focus.row && n.col === focus.col);
    return note?.values.includes(sign + value) ?? false;
  }

  sketching(mode?: GameMode): boolean {
    return mode == GameMode.SKETCH;
  }
}
