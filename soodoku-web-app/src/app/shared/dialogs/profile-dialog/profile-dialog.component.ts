import { NgIf } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { SELECT_USER_STATE } from '../../../store/app/app.selectors';
import { UserState } from '../../../store/app/app.state';
import { fadeInAnimation } from '../../animations/fade-in-animation';
import { ProfileDetailsComponent } from './profile-details/profile-details.component';
import { ProfileLoginComponent } from './profile-login/profile-login.component';

@Component({
  selector: 'soo-profile-dialog',
  standalone: true,
  imports: [NgIf, ProfileDetailsComponent, ProfileLoginComponent],
  templateUrl: './profile-dialog.component.html',
  styleUrl: './profile-dialog.component.scss',
  animations: [fadeInAnimation]
})
export class ProfileDialogComponent {

  @Output() result = new EventEmitter<boolean>();

  user!: UserState;

  constructor(private store: Store) {
    this.store.select(SELECT_USER_STATE).pipe(
      tap((state: UserState) => this.user = state)
    ).subscribe();
  }
}
