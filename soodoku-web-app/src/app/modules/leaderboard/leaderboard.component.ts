import { Component } from '@angular/core';
import { ViewAwareComponent } from '../../core/view/view.component';
import { ViewService } from '../../core/view/view.service';

@Component({
  selector: 'soo-leaderboard',
  standalone: true,
  imports: [],
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.scss'
})
export class LeaderboardComponent extends ViewAwareComponent {

  constructor(viewService: ViewService) {
    super(viewService);
  }
}
