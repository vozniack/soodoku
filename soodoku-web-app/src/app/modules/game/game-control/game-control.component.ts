import { AsyncPipe, NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { ViewAwareComponent } from '../../../core/view/view.component';
import { ViewService } from '../../../core/view/view.service';
import { IconComponent } from '../../../shared/components/icon/icon.component';
import { SELECT_BREAKPOINT } from '../../../store/app/app.selectors';
import { ACTION_GAME_REVERT, ACTION_GAME_WIPE } from '../../../store/app/game/game.actions';
import { GameState } from '../../../store/app/game/game.state';

@Component({
  selector: 'soo-game-control',
  standalone: true,
  imports: [IconComponent, AsyncPipe, NgIf],
  templateUrl: './game-control.component.html',
  styleUrl: './game-control.component.scss'
})
export class GameControlComponent extends ViewAwareComponent {

  @Input() gameState$!: Observable<GameState>;

  breakpoint!: Breakpoint;
  hints: number = 3;

  constructor(viewService: ViewService, private store: Store) {
    super(viewService);

    this.store.select(SELECT_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();
  }

  revert(): void {
    this.store.dispatch(ACTION_GAME_REVERT());
  }

  wipe(): void {
    this.store.dispatch(ACTION_GAME_WIPE());
  }
}
