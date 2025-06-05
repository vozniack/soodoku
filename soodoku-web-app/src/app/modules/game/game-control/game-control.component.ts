import { Component } from '@angular/core';
import { ViewAwareComponent } from '../../../core/view/view.component';
import { ViewService } from '../../../core/view/view.service';

@Component({
  selector: 'soo-game-control',
  standalone: true,
  imports: [],
  templateUrl: './game-control.component.html',
  styleUrl: './game-control.component.scss'
})
export class GameControlComponent extends ViewAwareComponent {

  constructor(viewService: ViewService) {
    super(viewService);
  }
}
