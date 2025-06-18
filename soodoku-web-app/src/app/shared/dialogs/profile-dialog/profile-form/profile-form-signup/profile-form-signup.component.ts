import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { AuthResponse, SignupRequest } from '../../../../../core/auth/auth.interface';
import { AuthService } from '../../../../../core/auth/auth.service';
import { ACTION_SHOW_SNACKBAR } from '../../../../../store/app/app.actions';
import { ACTION_AUTH_LOGIN } from '../../../../../store/app/auth/auth.actions';
import { ButtonComponent } from '../../../../components/button/button.component';
import { DividerComponent } from '../../../../components/divider/divider.component';
import { InputComponent } from '../../../../components/input/input.component';
import { emailRegex } from '../../../../const/regex.const';

@Component({
  selector: 'soo-profile-form-signup',
  standalone: true,
  imports: [InputComponent, ButtonComponent, DividerComponent],
  templateUrl: './profile-form-signup.component.html',
  styleUrl: '../profile-form.component.scss'
})
export class ProfileFormSignupComponent {

  @Input() result!: EventEmitter<boolean>;
  @Output() switchMode = new EventEmitter<'login'>();

  signupForm!: FormGroup;

  constructor(private store: Store, private formBuilder: FormBuilder, private authService: AuthService) {
    this.signupForm = this.formBuilder.group({
      email: new FormControl('', [Validators.required, Validators.pattern(emailRegex)]),
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required]),
      repeat: new FormControl('', [Validators.required]),
    });
  }

  signup(): void {
    const {repeat, ...formData} = this.signupForm.getRawValue();

    this.authService.signup(formData as SignupRequest).pipe(
      tap((response: AuthResponse) => this.store.dispatch(ACTION_AUTH_LOGIN({
        accessToken: response.accessToken,
        refreshToken: response.refreshToken
      }))),
      tap(() => this.store.dispatch(ACTION_SHOW_SNACKBAR({message: 'You have been signed up', icon: 'waving_hand'}))),
      tap(() => this.result.emit(true))
    ).subscribe();
  }

  onSwitchMode(): void {
    this.switchMode.emit('login');
  }

  getControl(controlName: string): FormControl {
    return this.signupForm.get(controlName) as FormControl;
  }
}
