import { inject, Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { tap } from 'rxjs/operators';
import { SnackbarService } from '../../core/snackbar/snackbar.service';
import { ACTION_SHOW_SNACKBAR } from './app.actions';

@Injectable()
export class AppEffects {

  private actions$ = inject(Actions);
  private snackbarService$ = inject(SnackbarService);

  showSnackbar$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(ACTION_SHOW_SNACKBAR),
        tap(({message, icon, duration}) => {
          this.snackbarService$.show(message, icon, duration);
        })
      ),
    {dispatch: false}
  );
}
