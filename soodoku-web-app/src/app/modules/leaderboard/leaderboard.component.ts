import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { View } from '../../core/view/view.const';
import { ACTION_SET_VIEW } from '../../store/app/app.actions';

@Component({
  selector: 'soo-leaderboard',
  standalone: true,
  imports: [],
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.scss'
})
export class LeaderboardComponent {

  constructor(private store: Store) {
  }

  setView(): void {
    this.store.dispatch(ACTION_SET_VIEW({view: View.HOME}));
  }
}
