import { Component } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { ViewAwareComponent } from '../../../core/view/view.component';
import { ViewService } from '../../../core/view/view.service';
import { IconComponent } from '../../../shared/components/icon/icon.component';
import { SELECT_BREAKPOINT } from '../../../store/app/app.selectors';

@Component({
  selector: 'soo-game-control',
  standalone: true,
  imports: [IconComponent],
  templateUrl: './game-control.component.html',
  styleUrl: './game-control.component.scss'
})
export class GameControlComponent extends ViewAwareComponent {

  breakpoint!: Breakpoint;
  hints: number = 3;

  constructor(private store: Store, viewService: ViewService) {
    super(viewService);

    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }
}
