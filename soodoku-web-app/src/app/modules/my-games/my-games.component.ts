import { Component } from '@angular/core';
import { ViewAwareComponent } from '../../core/view/view.component';
import { ViewService } from '../../core/view/view.service';

@Component({
  selector: 'soo-my-games',
  standalone: true,
  imports: [],
  templateUrl: './my-games.component.html',
  styleUrl: './my-games.component.scss'
})
export class MyGamesComponent extends ViewAwareComponent {

  constructor(viewService: ViewService) {
    super(viewService);
  }
}
