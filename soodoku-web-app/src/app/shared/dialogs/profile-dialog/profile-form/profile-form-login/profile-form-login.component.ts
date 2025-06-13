import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { LoginRequest, LoginResponse } from '../../../../../core/auth/auth.interface';
import { AuthService } from '../../../../../core/auth/auth.service';
import { ACTION_SHOW_SNACKBAR } from '../../../../../store/app/app.actions';
import { ACTION_AUTH_LOGIN } from '../../../../../store/app/auth/auth.actions';
import { ButtonComponent } from '../../../../components/button/button.component';
import { InputComponent } from '../../../../components/input/input.component';
import { emailRegex } from '../../../../const/regex.const';

@Component({
  selector: 'soo-profile-form-login',
  standalone: true,
  imports: [ButtonComponent, InputComponent],
  templateUrl: './profile-form-login.component.html',
  styleUrl: '../profile-form.component.scss'
})
export class ProfileFormLoginComponent {

  @Input() result!: EventEmitter<boolean>;
  @Output() switchMode = new EventEmitter<'signup'>();

  loginForm!: FormGroup;

  constructor(private store: Store, private formBuilder: FormBuilder, private authService: AuthService) {
    this.loginForm = this.formBuilder.group({
      email: new FormControl('', [Validators.required, Validators.pattern(emailRegex)]),
      password: new FormControl('', [Validators.required])
    });
  }

  login(): void {
    this.authService.login(this.loginForm.getRawValue() as LoginRequest).pipe(
      tap((response: LoginResponse) => this.store.dispatch(ACTION_AUTH_LOGIN({token: response.token}))),
      tap(() => this.store.dispatch(ACTION_SHOW_SNACKBAR({message: 'You have been logged in', icon: 'waving_hand'}))),
      tap(() => this.result.emit(true))
    ).subscribe();
  }

  onSwitchMode(): void {
    this.switchMode.emit('signup');
  }

  getControl(controlName: string): FormControl {
    return this.loginForm.get(controlName) as FormControl;
  }
}
