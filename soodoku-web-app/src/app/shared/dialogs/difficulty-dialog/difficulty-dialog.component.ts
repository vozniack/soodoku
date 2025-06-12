import { Component, EventEmitter, Output } from '@angular/core';
import { Store } from '@ngrx/store';
import { ACTION_GAME_NEW } from '../../../store/app/game/game.actions';
import { ButtonComponent } from '../../components/button/button.component';

@Component({
  selector: 'soo-difficulty-dialog',
  standalone: true,
  templateUrl: './difficulty-dialog.component.html',
  styleUrl: './difficulty-dialog.component.scss',
  imports: [ButtonComponent,]
})
export class DifficultyDialogComponent {

  @Output() result = new EventEmitter<boolean>();

  constructor(private store: Store) {
  }

  select(difficulty: string) {
    this.store.dispatch(ACTION_GAME_NEW({difficulty}));
    this.result.emit(true);
  }
}
