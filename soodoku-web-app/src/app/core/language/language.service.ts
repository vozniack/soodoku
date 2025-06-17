import { Injectable } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { select, Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { SELECT_APP_LANGUAGE } from '../../store/app/app.selectors';
import { Language } from './language.const';

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
      tap((language: Language) => console.log(language))
    ).subscribe();
  }
}
