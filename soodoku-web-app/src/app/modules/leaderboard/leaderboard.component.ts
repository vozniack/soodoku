import { Component } from '@angular/core';
import { View } from '../../core/view/view.const';
import { ViewService } from '../../core/view/view.service';

@Component({
  selector: 'soo-leaderboard',
  standalone: true,
  imports: [],
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.scss'
})
export class LeaderboardComponent {

  constructor(private viewService: ViewService) {
  }

  setView(): void {
    this.viewService.setView(View.HOME);
  }
}
