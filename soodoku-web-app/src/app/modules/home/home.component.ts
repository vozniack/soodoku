import { Component } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../core/breakpoint/breakpoint.interface';
import { DialogService } from '../../core/dialog/dialog.service';
import { User } from '../../core/user/user.interface';
import { View } from '../../core/view/view.const';
import { AvatarComponent } from '../../shared/components/avatar/avatar.component';
import { DividerComponent } from '../../shared/components/divider/divider.component';
import { LogoComponent } from '../../shared/components/logo/logo.component';
import { ViewService } from '../../core/view/view.service';
import { fadeInAnimation } from '../../shared/animations/fade-in-animation';
import { DifficultyDialogComponent } from '../../shared/dialogs/difficulty-dialog/difficulty-dialog.component';
import { SELECT_APP_BREAKPOINT } from '../../store/app/app.selectors';
import { SELECT_USER_STATE } from '../../store/app/user/user.selector';
import { UserState } from '../../store/app/user/user.state';
import { HomeGameComponent } from './home-game/home-game.component';
import { HomeSectionComponent } from './home-section/home-section.component';

@Component({
  selector: 'soo-home',
  standalone: true,
  imports: [LogoComponent, AvatarComponent, HomeSectionComponent, HomeGameComponent, DividerComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  animations: [fadeInAnimation]
})
export class HomeComponent {

  breakpoint!: Breakpoint;

  user?: User;

  constructor(private viewService: ViewService, private store: Store, private dialogService: DialogService) {
    this.store.select(SELECT_APP_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();

    this.store.select(SELECT_USER_STATE).pipe(
      tap((userState: UserState) => this.user = userState.user)
    ).subscribe();
  }

  newGame(): void {
    from(this.dialogService.open(DifficultyDialogComponent)).subscribe();
  }

  myGames(): void {
    if (this.user) {
      this.viewService.setView(View.MY_GAMES);
    }
  }

  leaderboard(): void {
    if (this.user) {
      this.viewService.setView(View.LEADERBOARD);
    }
  }
}
