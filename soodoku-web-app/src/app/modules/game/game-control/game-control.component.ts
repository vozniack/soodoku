import { AsyncPipe, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { filter, from, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { DialogService } from '../../../core/dialog/dialog.service';
import { IconComponent } from '../../../shared/components/icon/icon.component';
import { ConfirmationDialogComponent } from '../../../shared/dialogs/confirmation-dialog/confirmation-dialog.component';
import { SELECT_BREAKPOINT } from '../../../store/app/app.selectors';
import {
  ACTION_GAME_SURRENDER,
  ACTION_GAME_REVERT,
  ACTION_GAME_WIPE,
  ACTION_GAME_HINT
} from '../../../store/app/game/game.actions';
import { GameState } from '../../../store/app/game/game.state';
import { Game, Move } from '../game.interface';

@Component({
  selector: 'soo-game-control',
  standalone: true,
  imports: [IconComponent, AsyncPipe, NgIf],
  templateUrl: './game-control.component.html',
  styleUrl: './game-control.component.scss'
})
export class GameControlComponent {

  @Input() gameState$!: Observable<GameState>;

  breakpoint!: Breakpoint;

  constructor(private store: Store, private dialogService: DialogService) {
    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  revert(): void {
    this.store.dispatch(ACTION_GAME_REVERT());
  }

  wipe(): void {
    this.store.dispatch(ACTION_GAME_WIPE());
  }

  hint(): void {
    this.store.dispatch(ACTION_GAME_HINT());
  }

  surrender(): void {
    from(this.dialogService.open(ConfirmationDialogComponent, {
        closing: false,
        inputs: {
          title: 'Surrender? Are you sure?',
          description: 'You will not be able to return to this game.'
        }
      })
    ).pipe(
      filter((result: boolean) => result),
      tap(() => this.store.dispatch(ACTION_GAME_SURRENDER()))
    ).subscribe();
  }

  moves(game: Game): number {
    return game.moves.filter((move: Move) => (move.type == 'NORMAL') && !move.reverted).length;
  }
}
