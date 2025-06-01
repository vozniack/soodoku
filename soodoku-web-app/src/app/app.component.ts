import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { select, Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { ThemeService } from './core/theme/theme.service';
import { ToolbarComponent } from './core/toolbar/toolbar.component';
import { View } from './core/view/view.const';
import { GameComponent } from './modules/game/game.component';
import { HomeComponent } from './modules/home/home.component';
import { LeaderboardComponent } from './modules/leaderboard/leaderboard.component';
import { ProfileComponent } from './modules/profile/profile.component';
import { SELECT_VIEW } from './store/app/app.selectors';

@Component({
  selector: 'soo-root',
  standalone: true,
  imports: [ToolbarComponent, NgIf, HomeComponent, GameComponent, LeaderboardComponent, ProfileComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

  View = View;
  view!: View;

  constructor(private store: Store, private themeService: ThemeService) {
    this.store.pipe(
      takeUntilDestroyed(),
      select(SELECT_VIEW),
      tap((view: View) => this.view = view)
    ).subscribe();

    this.themeService.applyTheme();
  }
}
