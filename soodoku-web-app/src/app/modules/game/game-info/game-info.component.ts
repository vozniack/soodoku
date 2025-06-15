import { AsyncPipe, NgIf } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { BehaviorSubject, filter, from, interval, Observable, Subscription } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { DialogService } from '../../../core/dialog/dialog.service';
import { AvatarComponent } from '../../../shared/components/avatar/avatar.component';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { DifficultyDialogComponent } from '../../../shared/dialogs/difficulty-dialog/difficulty-dialog.component';
import { formatDurationBetween } from '../../../shared/functions/time.fuction';
import { SELECT_BREAKPOINT } from '../../../store/app/app.selectors';
import { GameState } from '../../../store/app/game/game.state';
import { Game } from '../game.interface';
import { updateElapsedTime } from './game-info.function';

@Component({
  selector: 'soo-game-info',
  standalone: true,
  imports: [ButtonComponent, AvatarComponent, NgIf, AsyncPipe],
  templateUrl: './game-info.component.html',
  styleUrl: './game-info.component.scss'
})
export class GameInfoComponent implements OnInit {

  @Input() gameState$!: Observable<GameState>;

  breakpoint!: Breakpoint;

  elapsedTime$ = new BehaviorSubject<string>('00:00');
  private timerSub?: Subscription;

  constructor(private store: Store, private dialogService: DialogService) {
    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  ngOnInit(): void {
    this.gameState$.pipe(
      filter((gameState: GameState) => !!gameState.game),
      tap((gameState: GameState) => this.startElapsedTimer(gameState.game!!.createdAt))
    ).subscribe();
  }

  newGame(): void {
    from(this.dialogService.open(DifficultyDialogComponent)).subscribe();
  }

  duration(game: Game): string {
    return formatDurationBetween(game.createdAt, game.finishedAt!!);
  }

  private startElapsedTimer(createdAt: string) {
    if (this.timerSub) {
      this.timerSub.unsubscribe();
    }

    updateElapsedTime(createdAt, this.elapsedTime$);

    this.timerSub = interval(1000).subscribe(() => {
      updateElapsedTime(createdAt, this.elapsedTime$);
    });
  }
}
