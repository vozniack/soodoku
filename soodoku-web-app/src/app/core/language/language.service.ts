import { Injectable } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { select, Store } from '@ngrx/store';
import { SELECT_APP_LANGUAGE } from '../../store/app/app.selectors';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {

  constructor(private store: Store) {
  }

  public applyLanguage() {
    this.store.pipe(
      takeUntilDestroyed(),
      select(SELECT_APP_LANGUAGE),
    ).subscribe();
  }
}
