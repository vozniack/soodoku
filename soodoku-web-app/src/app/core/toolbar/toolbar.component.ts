import { NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AvatarComponent } from '../../shared/components/avatar/avatar.component';
import { IconComponent } from '../../shared/components/icon/icon.component';
import { SocialDialogComponent } from '../../shared/dialogs/social-dialog/social-dialog.component';
import { HelpDialogComponent } from '../../shared/dialogs/help-dialog/help-dialog.component';
import { SettingsDialogComponent } from '../../shared/dialogs/settings-dialog/settings-dialog.component';
import { SELECT_APP_BREAKPOINT } from '../../store/app/app.selectors';
import { SELECT_USER_STATE } from '../../store/app/user/user.selector';
import { UserState } from '../../store/app/user/user.state';
import { Breakpoint } from '../breakpoint/breakpoint.interface';
import { DialogService } from '../dialog/dialog.service';
import { User } from '../user/user.interface';

@Component({
  selector: 'soo-toolbar',
  standalone: true,
  imports: [AvatarComponent, IconComponent, NgIf],
  templateUrl: './toolbar.component.html',
  styleUrl: './toolbar.component.scss'
})
export class ToolbarComponent {

  @Input() withUsername = false;

  breakpoint!: Breakpoint;
  user?: User;

  constructor(private store: Store, private dialogService: DialogService) {
    this.store.select(SELECT_APP_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();

    this.store.select(SELECT_USER_STATE).pipe(
      tap((userState: UserState) => this.user = userState.user)
    ).subscribe();
  }

  openSettings(): void {
    from(this.dialogService.open(SettingsDialogComponent)).subscribe();
  }

  openFriends(): void {
    from(this.dialogService.open(SocialDialogComponent)).subscribe();
  }

  openHelp(): void {
    from(this.dialogService.open(HelpDialogComponent)).subscribe();
  }
}
