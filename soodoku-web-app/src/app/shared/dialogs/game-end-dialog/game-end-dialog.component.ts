import { Component, EventEmitter, Output } from '@angular/core';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { Game } from '../../../modules/game/game.interface';
import { SELECT_GAME_STATE } from '../../../store/app/game/game.selectors';
import { GameState } from '../../../store/app/game/game.state';
import { ButtonComponent } from '../../components/button/button.component';

@Component({
  selector: 'soo-game-end-dialog',
  standalone: true,
  imports: [ButtonComponent],
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
    return this.game.missing == 0 ? 'Victory' : 'Defeat';
  }

  description(): string {
    return this.game.missing == 0 ? 'You have filled all cells!' : 'You missed ' + this.game.missing + ' cells!';
  }
}
