import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { filter, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { AvatarComponent } from '../../shared/components/avatar/avatar.component';
import { LogoComponent } from '../../shared/components/logo/logo.component';
import { fadeInAnimation } from '../../shared/animations/fade-in-animation';
import { SELECT_BREAKPOINT, SELECT_GAME_STATE } from '../../store/app/app.selectors';
import { GameState } from '../../store/app/app.state';
import { GameControlComponent } from './game-control/game-control.component';
import { GameInfoComponent } from './game-info/game-info.component';
import { GameBoardComponent } from './game-board/game-board.component';
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

  gameState$!: Observable<GameState>;

  constructor(private store: Store) {
    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();

    this.gameState$ = this.store.select(SELECT_GAME_STATE).pipe(
      takeUntilDestroyed(),
      filter(gameState => gameState?.game?.id !== undefined)
    );
  }
}
