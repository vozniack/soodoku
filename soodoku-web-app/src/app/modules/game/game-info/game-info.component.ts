import { AsyncPipe, NgIf } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { BehaviorSubject, filter, interval, Observable, Subscription } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { ToolbarComponent } from '../../../core/toolbar/toolbar.component';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { formatDuration } from '../../../shared/functions/time.fuction';
import { SELECT_APP_BREAKPOINT } from '../../../store/app/app.selectors';
import { ACTION_GAME_PAUSE, ACTION_GAME_RESUME } from '../../../store/app/game/game.actions';
import { GameState } from '../../../store/app/game/game.state';
import { Game, Session } from '../game.interface';
import { updateElapsedTime } from './game-info.function';

@Component({
  selector: 'soo-game-info',
  standalone: true,
  imports: [ButtonComponent, NgIf, AsyncPipe, ToolbarComponent],
  templateUrl: './game-info.component.html',
  styleUrl: './game-info.component.scss'
})
export class GameInfoComponent implements OnInit {

  @Input() gameState$!: Observable<GameState>;

  breakpoint!: Breakpoint;

  elapsedTime$ = new BehaviorSubject<string>('00:00');
  private timerSub?: Subscription;

  constructor(private store: Store) {
    this.store.select(SELECT_APP_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  ngOnInit(): void {
    this.gameState$.pipe(
      filter((gameState: GameState) => !!gameState.game),
      tap((gameState: GameState) => {
        const game = gameState.game!;
        this.startElapsedTimer(game.sessions, game.paused);
      })
    ).subscribe();
  }

  pauseOrResume(paused: boolean): void {
    this.store.dispatch(paused ? ACTION_GAME_RESUME() : ACTION_GAME_PAUSE());
  }

  duration(game: Game): string {
    return formatDuration(game.sessions);
  }

  private startElapsedTimer(sessions: Session[], isPaused: boolean) {
    if (this.timerSub) {
      this.timerSub.unsubscribe();
    }

    updateElapsedTime(sessions, this.elapsedTime$);

    if (!isPaused) {
      this.timerSub = interval(1000).subscribe(() => {
        updateElapsedTime(sessions, this.elapsedTime$);
      });
    }
  }
}
