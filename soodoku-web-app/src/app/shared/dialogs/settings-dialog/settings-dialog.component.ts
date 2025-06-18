import { Component } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Store } from '@ngrx/store';
import { merge, switchMap } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Language } from '../../../core/language/language.const';
import { mapLanguage } from '../../../core/language/language.function';
import { Theme } from '../../../core/theme/theme.const';
import { ACTION_SET_LANGUAGE, ACTION_SET_THEME } from '../../../store/app/app.actions';
import { SELECT_APP_STATE } from '../../../store/app/app.selectors';
import { AppState } from '../../../store/app/app.state';
import { DividerComponent } from '../../components/divider/divider.component';
import { SwitchComponent } from '../../components/switch/switch.component';
import { SettingsDialogLanguageComponent } from './settings-dialog-language/settings-dialog-language.component';

@Component({
  selector: 'soo-settings-dialog',
  standalone: true,
  imports: [SwitchComponent, DividerComponent, SettingsDialogLanguageComponent],
  templateUrl: './settings-dialog.component.html',
  styleUrl: './settings-dialog.component.scss'
})
export class SettingsDialogComponent {

  Language = Language;
  language!: Language;

  languageForm!: FormControl;
  darkThemeForm!: FormControl;

  constructor(private store: Store) {
    this.store.select(SELECT_APP_STATE).pipe(
      tap((appState: AppState) => {
        this.language = appState.language;

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
  }

  selectLanguage(language: string) {
    this.store.dispatch(ACTION_SET_LANGUAGE({language: mapLanguage(language)}));
  }
}
