import { Component, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import { View } from '../../../core/view/view.const';
import { ACTION_SET_VIEW } from '../../../store/app/app.actions';

@Component({
  selector: 'soo-logo',
  standalone: true,
  imports: [],
  templateUrl: './logo.component.html',
  styleUrl: './logo.component.scss'
})
export class LogoComponent {

  @Input() width: string = '100%';
  @Input() height: string = '100%';

  constructor(private store: Store) {
  }

  home(): void {
    this.store.dispatch(ACTION_SET_VIEW({view: View.HOME}));
  }
}
