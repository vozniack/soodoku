import { AsyncPipe, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { combineLatest } from 'rxjs';
import { map } from 'rxjs/operators';
import { View } from '../../../core/view/view.const';
import { ViewService } from '../../../core/view/view.service';
import { fadeInAnimation } from '../../../shared/animations/fade-in-animation';
import { ButtonComponent } from '../../../shared/components/button/button.component';
import { DividerComponent } from '../../../shared/components/divider/divider.component';
import { SELECT_GAME_STATE } from '../../../store/app/game/game.selectors';
import { GameState } from '../../../store/app/game/game.state';
import { SELECT_USER_STATE } from '../../../store/app/user/user.selector';
import { UserState } from '../../../store/app/user/user.state';

@Component({
  selector: 'soo-home-game',
  standalone: true,
  imports: [NgIf, ButtonComponent, AsyncPipe, DividerComponent],
  templateUrl: './home-game.component.html',
  styleUrl: './home-game.component.scss',
  animations: [fadeInAnimation]
})
export class HomeGameComponent {

  readonly game$;

  constructor(private viewService: ViewService, private store: Store) {
    this.game$ = combineLatest([
      this.store.select(SELECT_USER_STATE),
      this.store.select(SELECT_GAME_STATE),
    ]).pipe(
      map(([userState, gameState]) =>
        this.isGameCorrect(userState, gameState) ? gameState.game : undefined
      )
    );
  }

  private isGameCorrect(userState: UserState, gameState: GameState): boolean | undefined {
    return userState.user && gameState.game && gameState.game.userId === userState.user.id && !gameState.game.finishedAt;
  }

  openGame(): void {
    this.viewService.setView(View.GAME);
  }
}
