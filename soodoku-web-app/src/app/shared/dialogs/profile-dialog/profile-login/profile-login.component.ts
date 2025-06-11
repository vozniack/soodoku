import { Component, EventEmitter, Input } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { LoginRequest, LoginResponse } from '../../../../core/auth/auth.interface';
import { AuthService } from '../../../../core/auth/auth.service';
import { ACTION_AUTH_LOGIN } from '../../../../store/app/auth/auth.actions';
import { ButtonComponent } from '../../../components/button/button.component';
import { InputComponent } from '../../../components/input/input.component';
import { emailRegex } from '../../../const/regex.const';

@Component({
  selector: 'soo-profile-login',
  standalone: true,
  imports: [InputComponent, ButtonComponent],
  templateUrl: './profile-login.component.html',
  styleUrl: './profile-login.component.scss'
})
export class ProfileLoginComponent {

  @Input() result!: EventEmitter<boolean>;

  form!: FormGroup;

  constructor(private store: Store, private formBuilder: FormBuilder, private authService: AuthService) {
    this.form = this.formBuilder.group({
      email: new FormControl('', [Validators.required, Validators.pattern(emailRegex)]  ),
      password: new FormControl('', [Validators.required])
    });
  }

  login(): void {
    this.authService.login(this.form.getRawValue() as LoginRequest).pipe(
      tap((response: LoginResponse) => this.store.dispatch(ACTION_AUTH_LOGIN({token: response.token}))),
      tap(() => this.result.emit(true))
    ).subscribe();
  }

  getControl(controlName: string): FormControl {
    return this.form.get(controlName) as FormControl;
  }
}
