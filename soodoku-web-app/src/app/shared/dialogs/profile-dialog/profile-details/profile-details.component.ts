import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { ACTION_LOGOUT } from '../../../../store/app/app.actions';
import { ButtonComponent } from '../../../components/button/button.component';

@Component({
  selector: 'soo-profile-details',
  standalone: true,
  imports: [ButtonComponent],
  templateUrl: './profile-details.component.html',
  styleUrl: './profile-details.component.scss'
})
export class ProfileDetailsComponent {

  constructor(private store: Store) {
  }

  logout(): void {
    this.store.dispatch(ACTION_LOGOUT());
  }
}
