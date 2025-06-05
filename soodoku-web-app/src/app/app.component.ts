import { NgIf } from '@angular/common';
import { AfterViewInit, Component } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { select, Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { ThemeService } from './core/theme/theme.service';
import { View } from './core/view/view.const';
import { GameComponent } from './modules/game/game.component';
import { HomeComponent } from './modules/home/home.component';
import { LeaderboardComponent } from './modules/leaderboard/leaderboard.component';
import { MyGamesComponent } from './modules/my-games/my-games.component';
import { ProfileComponent } from './modules/profile/profile.component';
import { SELECT_VIEW } from './store/app/app.selectors';

@Component({
  selector: 'soo-root',
  standalone: true,
  imports: [NgIf, HomeComponent, GameComponent, LeaderboardComponent, ProfileComponent, MyGamesComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements AfterViewInit {

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

  ngAfterViewInit(): void {
    this.setViewportHeight();

    let resizeTimeout: any;

    window.addEventListener('resize', () => {
      clearTimeout(resizeTimeout);

      resizeTimeout = setTimeout(() => {
        this.setViewportHeight();
      }, 150);
    });
  }

  private setViewportHeight(): void {
    if (typeof window !== 'undefined') {
      document.documentElement.style.setProperty('--vh', `${window.innerHeight * 0.01}px`);
    }
  }
}
