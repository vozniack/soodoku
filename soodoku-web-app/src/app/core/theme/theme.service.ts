import { Injectable } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { select, Store } from '@ngrx/store';
import { Subject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ACTION_USER_SET_THEME } from '../../store/app/user/user.actions';
import { SELECT_USER_THEME } from '../../store/app/user/user.selector';
import { Theme } from './theme.const';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  theme!: Theme;
  themeChange: Subject<Theme> = new Subject();

  constructor(private store: Store) {
    this.applyTheme();
  }

  public applyTheme() {
    this.store.pipe(
      takeUntilDestroyed(),
      select(SELECT_USER_THEME),
      tap((theme: Theme) => document.body.setAttribute('theme', this.theme = theme))
    ).subscribe();
  }

  public setTheme(theme: Theme) {
    this.store.dispatch(ACTION_USER_SET_THEME({theme: this.theme = theme}));
    this.themeChange.next(this.theme);
  }
}
