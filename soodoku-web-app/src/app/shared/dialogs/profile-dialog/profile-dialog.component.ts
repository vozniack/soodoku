import { NgIf } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { SELECT_AUTH_STATE } from '../../../store/app/auth/auth.selectors';
import { AuthState } from '../../../store/app/auth/auth.state';
import { fadeInAnimation } from '../../animations/fade-in-animation';
import { ProfileDetailsComponent } from './profile-details/profile-details.component';
import { ProfileFormComponent } from './profile-form/profile-form.component';

@Component({
  selector: 'soo-profile-dialog',
  standalone: true,
  imports: [NgIf, ProfileDetailsComponent, ProfileFormComponent],
  templateUrl: './profile-dialog.component.html',
  styleUrl: './profile-dialog.component.scss',
  animations: [fadeInAnimation]
})
export class ProfileDialogComponent {

  @Output() result = new EventEmitter<boolean>();

  authState!: AuthState;

  constructor(private store: Store) {
    this.store.select(SELECT_AUTH_STATE).pipe(
      tap((authState: AuthState) => this.authState = authState)
    ).subscribe();
  }
}
