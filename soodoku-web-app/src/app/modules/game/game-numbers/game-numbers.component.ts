import { AsyncPipe, NgForOf, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { filter, Observable, take } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { SELECT_BREAKPOINT } from '../../../store/app/app.selectors';
import { GameState } from '../../../store/app/game/game.state';
import { setGameActionBuilder } from '../game.function';
import { Game } from '../game.interface';
import { GameService } from '../game.service';

@Component({
  selector: 'soo-game-numbers',
  standalone: true,
  imports: [NgForOf, AsyncPipe, NgIf],
  templateUrl: './game-numbers.component.html',
  styleUrl: './game-numbers.component.scss'
})
export class GameNumbersComponent {

  @Input() gameState$!: Observable<GameState>;

  breakpoint!: Breakpoint;

  numbers: number[] = [...Array(9).keys()].map(i => i + 1);

  constructor(private store: Store, private gameService: GameService) {
    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  move(value: number): void {
    this.gameState$.pipe(
      take(1),
      filter((gameState) => !!gameState.game && !!gameState.focus),
      switchMap((gameState: GameState) =>
        this.gameService.move(gameState.game!!.id, gameState.focus!.row, gameState.focus!.col, value).pipe(
          map((updatedGame: Game) => ({updatedGame, focus: gameState.focus!}))
        )
      ),
      tap(({updatedGame, focus}) => {
        this.store.dispatch(setGameActionBuilder(updatedGame, focus));
      })
    ).subscribe();
  }
}
