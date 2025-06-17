import { NgIf } from '@angular/common';
import { Component, EventEmitter, Input } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { merge, switchMap } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Language } from '../../../../core/language/language.const';
import { Theme } from '../../../../core/theme/theme.const';
import { User } from '../../../../core/user/user.interface';
import { UserService } from '../../../../core/user/user.service';
import { ACTION_SET_LANGUAGE, ACTION_SET_THEME, ACTION_SHOW_SNACKBAR } from '../../../../store/app/app.actions';
import { SELECT_APP_STATE } from '../../../../store/app/app.selectors';
import { AppState } from '../../../../store/app/app.state';
import { ACTION_AUTH_LOGOUT } from '../../../../store/app/auth/auth.actions';
import { ACTION_USER_SET } from '../../../../store/app/user/user.actions';
import { SELECT_USER_STATE } from '../../../../store/app/user/user.selector';
import { UserState } from '../../../../store/app/user/user.state';
import { ButtonComponent } from '../../../components/button/button.component';
import { DividerComponent } from '../../../components/divider/divider.component';
import { InputComponent } from '../../../components/input/input.component';
import { SelectComponent } from '../../../components/select/select.component';
import { SwitchComponent } from '../../../components/switch/switch.component';

@Component({
  selector: 'soo-profile-details',
  standalone: true,
  imports: [ButtonComponent, NgIf, InputComponent, DividerComponent, SelectComponent, SwitchComponent],
  templateUrl: './profile-details.component.html',
  styleUrl: './profile-details.component.scss'
})
export class ProfileDetailsComponent {

  @Input() result!: EventEmitter<boolean>;

  userState!: UserState;

  languageForm!: FormControl;
  darkThemeForm!: FormControl;

  emailForm!: FormControl;
  usernameForm!: FormControl;
  passwordForm!: FormGroup;

  languageOptions: { name: string; value: any }[] = [
    {name: 'Polski', value: 'pl_PL'},
    {name: 'English', value: 'en_EN'}
  ];

  constructor(private store: Store, private formBuilder: FormBuilder, private userService: UserService) {
    this.store.select(SELECT_APP_STATE).pipe(
      tap((appState: AppState) => {
        this.languageForm = new FormControl(appState.language, [Validators.required]);
        this.darkThemeForm = new FormControl(appState.theme === Theme.DARK, [Validators.required]);
      }),
      switchMap(() => merge(
        this.languageForm.valueChanges.pipe(
          map((language: Language) => ACTION_SET_LANGUAGE({language}))
        ),

        this.darkThemeForm.valueChanges.pipe(
          map((darkMode: boolean) =>
            ACTION_SET_THEME({theme: darkMode ? Theme.DARK : Theme.LIGHT})
          )
        )
      ))
    ).subscribe(action => this.store.dispatch(action));

    this.store.select(SELECT_USER_STATE).pipe(
      tap((userState: UserState) => this.userState = userState),
      tap(() => {
        this.emailForm = new FormControl(this.userState.user?.email);
        this.emailForm.disable();

        this.usernameForm = new FormControl(this.userState.user?.username, [Validators.required]);

        this.passwordForm = this.formBuilder.group({
          password: new FormControl('', [Validators.required]),
          repeat: new FormControl('', [Validators.required]),
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
