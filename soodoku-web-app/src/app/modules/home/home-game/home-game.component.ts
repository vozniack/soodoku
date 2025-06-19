import { AsyncPipe, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { select, Store } from '@ngrx/store';
import { Observable, of } from 'rxjs';
import { catchError, distinctUntilChanged, map, switchMap } from 'rxjs/operators';
import { UserService } from '../../../core/user/user.service';
import { fadeInAnimation } from '../../../shared/animations/fade-in-animation';
import { DividerComponent } from '../../../shared/components/divider/divider.component';
import { GameTileComponent } from '../../../shared/components/game-tile/game-tile.component';
import { SELECT_AUTH_STATE } from '../../../store/app/auth/auth.selectors';
import { AuthState } from '../../../store/app/auth/auth.state';
import { Game } from '../../game/game.interface';

@Component({
  selector: 'soo-home-game',
  standalone: true,
  imports: [NgIf, DividerComponent, GameTileComponent, AsyncPipe],
  templateUrl: './home-game.component.html',
  styleUrl: './home-game.component.scss',
  animations: [fadeInAnimation]
})
export class HomeGameComponent {

  game$!: Observable<Game | null>;

  constructor(private store: Store, private userService: UserService) {
    this.game$ = this.store.pipe(
      select(SELECT_AUTH_STATE),
      map((authState: AuthState) => authState.accessToken),
      distinctUntilChanged(),
      switchMap((token) => {
        if (!token) return of(null);

        return this.userService.getLastGame().pipe(
          catchError(() => of(null))
        );
      })
    );
  }
}
