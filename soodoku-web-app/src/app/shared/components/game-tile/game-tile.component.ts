import { DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import { View } from '../../../core/view/view.const';
import { Game } from '../../../modules/game/game.interface';
import { ACTION_SET_VIEW } from '../../../store/app/app.actions';
import { ACTION_GAME_SET } from '../../../store/app/game/game.actions';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'soo-game-tile',
  standalone: true,
  imports: [IconComponent, DatePipe],
  templateUrl: './game-tile.component.html',
  styleUrl: './game-tile.component.scss'
})
export class GameTileComponent {

  @Input() game!: Game;

  constructor(private store: Store) {
  }

  openGame(): void {
    this.store.dispatch(ACTION_GAME_SET({game: this.game, focus: undefined, sketch: false}));
    this.store.dispatch(ACTION_SET_VIEW({view: View.GAME}));
  }

  getTime(): string {
    return this.game.updatedAt ?? this.game.createdAt;
  }

  getState(): string {
    return this.game.finishedAt == null ? 'ongoing' : this.game.missing == 0 ? 'won' : 'lost';
  }

  getIcon(): string {
    switch (this.getState()) {
      case 'won':
        return 'trophy';

      case 'lost':
        return 'skull';

      default:
        return 'play_arrow';
    }
  }
}
