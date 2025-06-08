import { AsyncPipe, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { filter, map, switchMap, take, tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { ViewAwareComponent } from '../../../core/view/view.component';
import { ViewService } from '../../../core/view/view.service';
import { IconComponent } from '../../../shared/components/icon/icon.component';
import { SELECT_BREAKPOINT } from '../../../store/app/app.selectors';
import { GameState } from '../../../store/app/app.state';
import { setGameActionBuilder } from '../game.function';
import { Game } from '../game.interface';
import { GameService } from '../game.service';

@Component({
  selector: 'soo-game-control',
  standalone: true,
  imports: [IconComponent, AsyncPipe, NgIf],
  templateUrl: './game-control.component.html',
  styleUrl: './game-control.component.scss'
})
export class GameControlComponent extends ViewAwareComponent {

  @Input() gameState$!: Observable<GameState>;

  breakpoint!: Breakpoint;
  hints: number = 3;

  constructor(viewService: ViewService, private store: Store, private gameService: GameService) {
    super(viewService);

    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  revert(): void {
    this.gameState$.pipe(
      take(1),
      filter((gameState) => !!gameState.activeCell),
      switchMap((gameState) =>
        this.gameService.revert(gameState.game.id).pipe(
          map((updatedGame: Game) => ({updatedGame, activeCell: gameState.activeCell}))
        )
      ),
      tap(({updatedGame, activeCell}) => {
        this.store.dispatch(setGameActionBuilder(updatedGame, activeCell));
      })
    ).subscribe();
  }

  wipe(): void {
    this.gameState$.pipe(
      take(1),
      filter((gameState) => !!gameState.activeCell),
      switchMap((gameState) =>
        this.gameService.move(gameState.game.id, gameState.activeCell!.row, gameState.activeCell!.col, 0).pipe(
          map((updatedGame: Game) => ({updatedGame, activeCell: gameState.activeCell!}))
        )
      ),
      tap(({updatedGame, activeCell}) => {
        this.store.dispatch(setGameActionBuilder(updatedGame, activeCell));
      })
    ).subscribe();
  }
}
