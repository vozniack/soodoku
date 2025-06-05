import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { SELECT_BREAKPOINT } from '../../store/app/app.selectors';
import { Breakpoint } from './breakpoint.interface';

export abstract class BreakpointAwareComponent {

  protected breakpoint!: Breakpoint;

  protected constructor(private store: Store) {
    this.store.select(SELECT_BREAKPOINT).pipe(
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }
}
