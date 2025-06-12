import { Component, EventEmitter, Output } from '@angular/core';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { Game } from '../../../modules/game/game.interface';
import { SELECT_GAME_STATE } from '../../../store/app/game/game.selectors';
import { GameState } from '../../../store/app/game/game.state';
import { ButtonComponent } from '../../components/button/button.component';
import { IconComponent } from '../../components/icon/icon.component';

@Component({
  selector: 'soo-game-end-dialog',
  standalone: true,
  imports: [ButtonComponent, IconComponent],
  templateUrl: './game-end-dialog.component.html',
  styleUrl: './game-end-dialog.component.scss'
})
export class GameEndDialogComponent {

  @Output() result = new EventEmitter<boolean>();

  game!: Game;

  constructor(private store: Store) {
    this.store.select(SELECT_GAME_STATE).pipe(
      tap((gameState: GameState) => this.game = gameState.game!!)
    ).subscribe();
  }

  title(): string {
    return this.game.missing == 0 ? 'Victory!' : 'Defeat';
  }

  description(): string {
    return this.game.missing == 0 ? 'Good job!' : 'Try once again!'
   }

  icon(): string {
    return this.game.missing == 0 ? 'trophy' : 'skull';
  }

  duration(): string {
    return this.formatDurationBetween(this.game.createdAt, this.game.finishedAt!!);
  }

  private formatDurationBetween(date1: string, date2: string): string {
    const parseSafeDate = (input: string) => new Date(input.replace(/(\.\d{3})\d+/, '$1'));

    const d1 = parseSafeDate(date1);
    const d2 = parseSafeDate(date2);

    const diffMs = d2.getTime() - d1.getTime();
    const totalSeconds = Math.floor(Math.abs(diffMs) / 1000);

    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const seconds = totalSeconds % 60;

    const pad = (n: number) => String(n).padStart(2, '0');

    return hours > 0
      ? `${pad(hours)}:${pad(minutes)}:${pad(seconds)}`
      : `${pad(minutes)}:${pad(seconds)}`;
  }

}
