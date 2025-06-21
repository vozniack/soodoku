import { NgForOf, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { catchError, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { ToolbarComponent } from '../../core/toolbar/toolbar.component';
import { View } from '../../core/view/view.const';
import { fadeInAnimation } from '../../shared/animations/fade-in-animation';
import { ButtonGroupComponent } from '../../shared/components/button-group/button-group.component';
import { GroupButton } from '../../shared/components/button-group/button-group.interface';
import { ButtonComponent } from '../../shared/components/button/button.component';
import { GameTileComponent } from '../../shared/components/game-tile/game-tile.component';
import { LogoComponent } from '../../shared/components/logo/logo.component';
import { InfiniteScrollDirective } from '../../shared/directives/infinite-scroll.directive';
import { ACTION_SET_VIEW } from '../../store/app/app.actions';
import { SELECT_APP_BREAKPOINT } from '../../store/app/app.selectors';
import { Game } from '../game/game.interface';
import { GameService } from '../game/game.service';

@Component({
  selector: 'soo-my-games',
  standalone: true,
  imports: [LogoComponent, ToolbarComponent, ButtonComponent, GameTileComponent, NgIf, ButtonGroupComponent, InfiniteScrollDirective, NgForOf],
  templateUrl: './my-games.component.html',
  styleUrl: './my-games.component.scss',
  animations: [fadeInAnimation]
})
export class MyGamesComponent implements OnInit {

  breakpoint!: Breakpoint;

  gameType!: GroupButton;
  gameTypes: GroupButton[] = [
    {
      text: 'Ongoing',
      value: false
    },
    {
      text: 'Archived',
      value: true
    },
  ];

  games: Game[] = [];

  page = 0;
  size = 24;
  isLast = false;
  isLoading = false;

  constructor(private store: Store, private gameService: GameService) {
    this.store.select(SELECT_APP_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();

    this.gameType = this.gameTypes[0];
  }

  ngOnInit(): void {
    this.loadGames();
  }

  home(): void {
    this.store.dispatch(ACTION_SET_VIEW({view: View.HOME}));
  }

  onGameTypeChange(gameType: GroupButton) {
    this.gameType = gameType;

    this.games = [];
    this.page = 0;
    this.isLast = false;

    this.loadGames();
  }

  loadGames(): void {
    if (this.isLoading || this.isLast) {
      return;
    }

    this.isLoading = true;

    this.gameService.getOngoing(this.page, this.size).pipe(
      tap(slice => {
        this.games.push(...slice.content);
        this.page += 1;
        this.isLast = slice.last;
        this.isLoading = false;
      }),
      catchError(() => {
        this.isLoading = false;
        return of(null);
      })
    ).subscribe();
  }
}
