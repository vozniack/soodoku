import { Injectable } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { select, Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { SELECT_APP_THEME } from '../../store/app/app.selectors';
import { Theme } from './theme.const';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  constructor(private store: Store) {
  }

  public applyTheme() {
    this.store.pipe(
      takeUntilDestroyed(),
      select(SELECT_APP_THEME),
      tap((theme: Theme) => document.body.setAttribute('theme', theme))
    ).subscribe();
  }
}
