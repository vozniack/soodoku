import { Component } from '@angular/core';
import { View } from '../../core/view/view.const';
import { ViewService } from '../../core/view/view.service';

@Component({
  selector: 'soo-my-games',
  standalone: true,
  imports: [],
  templateUrl: './my-games.component.html',
  styleUrl: './my-games.component.scss'
})
export class MyGamesComponent {

  constructor(private viewService: ViewService) {
  }

  setView(): void {
    this.viewService.setView(View.HOME);
  }
}
