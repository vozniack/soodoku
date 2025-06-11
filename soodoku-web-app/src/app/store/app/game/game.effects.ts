import { inject, Injectable } from '@angular/core';
import { Actions } from '@ngrx/effects';
import { Store } from '@ngrx/store';

@Injectable()
export class GameEffects {

  private actions$ = inject(Actions);
  private store$ = inject(Store);
}
