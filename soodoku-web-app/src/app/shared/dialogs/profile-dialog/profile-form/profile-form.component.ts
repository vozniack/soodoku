import { NgIf } from '@angular/common';
import { Component, EventEmitter, Input } from '@angular/core';
import { fadeInAnimation } from '../../../animations/fade-in-animation';
import { ProfileFormLoginComponent } from './profile-form-login/profile-form-login.component';
import { ProfileFormSignupComponent } from './profile-form-signup/profile-form-signup.component';

@Component({
  selector: 'soo-profile-form',
  standalone: true,
  imports: [NgIf, ProfileFormLoginComponent, ProfileFormSignupComponent],
  templateUrl: './profile-form.component.html',
  styleUrl: './profile-form.component.scss',
  animations: [fadeInAnimation]
})
export class ProfileFormComponent {

  @Input() result!: EventEmitter<boolean>;

  mode: 'login' | 'signup' = 'login';

  onSwitchMode(newMode: 'login' | 'signup') {
    this.mode = newMode;
  }
}
