import { Injectable } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { select, Store } from '@ngrx/store';
import { Subject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ACTION_SET_THEME } from '../../store/app/app.actions';
import { SELECT_THEME } from '../../store/app/app.selectors';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  theme!: string;
  themeChange: Subject<string> = new Subject();

  constructor(private store: Store) {
    this.applyTheme();
  }

  public applyTheme() {
    this.store.pipe(
      takeUntilDestroyed(),
      select(SELECT_THEME),
      tap((theme: string) => document.body.setAttribute('theme', this.theme = theme))
    ).subscribe();
  }

  public setTheme(theme: string) {
    this.store.dispatch(ACTION_SET_THEME({theme: this.theme = theme}));
    this.themeChange.next(this.theme);
  }
}
