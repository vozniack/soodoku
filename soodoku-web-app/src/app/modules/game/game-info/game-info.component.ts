import { AsyncPipe, NgIf } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { BehaviorSubject, filter, from, interval, Observable, Subscription } from 'rxjs';
import { switchMap, tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { DialogService } from '../../../core/dialog/dialog.service';
import { View } from '../../../core/view/view.const';
import { AvatarComponent } from '../../../shared/components/avatar/avatar.component';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { DifficultyDialogComponent } from '../../../shared/dialogs/difficulty-dialog/difficulty-dialog.component';
import { ACTION_SET_GAME, ACTION_SET_VIEW } from '../../../store/app/app.actions';
import { SELECT_BREAKPOINT } from '../../../store/app/app.selectors';
import { GameState } from '../../../store/app/app.state';
import { Game } from '../game.interface';
import { GameService } from '../game.service';
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

  constructor(private store: Store, private dialogService: DialogService, private gameService: GameService) {
    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  ngOnInit(): void {
    this.gameState$.pipe(
      tap(gameState => this.startElapsedTimer(gameState.game.createdAt))
    ).subscribe();
  }

  newGame(): void {
    from(this.dialogService.open(DifficultyDialogComponent))
      .pipe(
        filter((difficulty): difficulty is string => !!difficulty),
        switchMap(difficulty => this.gameService.new(difficulty)),
        tap((game: Game) => {
          this.store.dispatch(ACTION_SET_GAME({game: game, activeCell: undefined}));
          this.store.dispatch(ACTION_SET_VIEW({view: View.GAME}));
        })
      ).subscribe();
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
