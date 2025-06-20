import { NgIf } from '@angular/common';
import { Component, EventEmitter, Input } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { User } from '../../../../core/user/user.interface';
import { UserService } from '../../../../core/user/user.service';
import { ACTION_SHOW_SNACKBAR } from '../../../../store/app/app.actions';
import { ACTION_AUTH_LOGOUT } from '../../../../store/app/auth/auth.actions';
import { ACTION_USER_SET } from '../../../../store/app/user/user.actions';
import { SELECT_USER_STATE } from '../../../../store/app/user/user.selector';
import { UserState } from '../../../../store/app/user/user.state';
import { ButtonComponent } from '../../../components/button/button.component';
import { DividerComponent } from '../../../components/divider/divider.component';
import { InputComponent } from '../../../components/input/input.component';
import { passwordRegex } from '../../../const/regex.const';

@Component({
  selector: 'soo-profile-details',
  standalone: true,
  imports: [ButtonComponent, NgIf, InputComponent, DividerComponent],
  templateUrl: './profile-details.component.html',
  styleUrl: './profile-details.component.scss'
})
export class ProfileDetailsComponent {

  @Input() result!: EventEmitter<boolean>;

  userState!: UserState;

  emailForm!: FormControl;
  usernameForm!: FormControl;
  passwordForm!: FormGroup;

  constructor(private store: Store, private formBuilder: FormBuilder, private userService: UserService) {
    this.store.select(SELECT_USER_STATE).pipe(
      tap((userState: UserState) => this.userState = userState),
      tap(() => {
        this.emailForm = new FormControl(this.userState.user?.email);
        this.emailForm.disable();

        this.usernameForm = new FormControl(this.userState.user?.username, [Validators.required]);

        this.passwordForm = this.formBuilder.group({
          password: new FormControl('', [Validators.required, Validators.pattern(passwordRegex)]),
          repeat: new FormControl('', [Validators.required, Validators.pattern(passwordRegex)]),
        });
      })
    ).subscribe();
  }

  updateUsername(): void {
    this.userService.updateUsername(this.userState.user!!.id, {username: this.usernameForm.getRawValue()}).pipe(
      tap((user: User) => this.store.dispatch(ACTION_USER_SET({user}))),
      tap(() => this.store.dispatch(ACTION_SHOW_SNACKBAR({message: 'Username updated', icon: 'person'})))
    ).subscribe();
  }

  updatePassword(): void {
    this.userService.updatePassword(this.userState.user!!.id, {password: this.passwordForm.getRawValue().password}).pipe(
      tap((user: User) => this.store.dispatch(ACTION_USER_SET({user}))),
      tap(() => this.store.dispatch(ACTION_SHOW_SNACKBAR({message: 'Password updated', icon: 'lock'})))
    ).subscribe();
  }

  logout(): void {
    this.store.dispatch(ACTION_AUTH_LOGOUT());
    this.result.emit(true);
  }

  getControl(controlName: string): FormControl {
    return this.passwordForm.get(controlName) as FormControl;
  }

  protected readonly open = open;
}
