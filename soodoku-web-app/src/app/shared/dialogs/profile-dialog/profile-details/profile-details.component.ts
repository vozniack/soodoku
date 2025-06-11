import { Component, EventEmitter, Input } from '@angular/core';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { ACTION_AUTH_LOGOUT } from '../../../../store/app/auth/auth.actions';
import { SELECT_USER_STATE } from '../../../../store/app/user/user.selector';
import { UserState } from '../../../../store/app/user/user.state';
import { ButtonComponent } from '../../../components/button/button.component';

@Component({
  selector: 'soo-profile-details',
  standalone: true,
  imports: [ButtonComponent],
  templateUrl: './profile-details.component.html',
  styleUrl: './profile-details.component.scss'
})
export class ProfileDetailsComponent {

  @Input() result!: EventEmitter<boolean>;

  userState!: UserState;

  constructor(private store: Store) {
    this.store.select(SELECT_USER_STATE).pipe(
      tap((userState: UserState) => this.userState = userState)
    ).subscribe();
  }

  logout(): void {
    this.store.dispatch(ACTION_AUTH_LOGOUT());
    this.result.emit(true);
  }
}
