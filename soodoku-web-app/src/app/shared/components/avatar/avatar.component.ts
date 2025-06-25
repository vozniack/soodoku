import { NgIf } from '@angular/common';
import { Component, Input } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Breakpoint } from '../../../core/breakpoint/breakpoint.interface';
import { DialogService } from '../../../core/dialog/dialog.service';
import { SELECT_APP_BREAKPOINT } from '../../../store/app/app.selectors';
import { SELECT_USER_STATE } from '../../../store/app/user/user.selector';
import { UserState } from '../../../store/app/user/user.state';
import { ProfileDialogComponent } from '../../dialogs/profile-dialog/profile-dialog.component';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'soo-avatar',
  standalone: true,
  imports: [IconComponent, NgIf],
  templateUrl: './avatar.component.html',
  styleUrl: './avatar.component.scss'
})
export class AvatarComponent {

  @Input() withUsername = false;

  breakpoint!: Breakpoint;
  username!: string;

  constructor(private store: Store, private dialogService: DialogService) {
    this.store.select(SELECT_APP_BREAKPOINT).pipe(
      takeUntilDestroyed(),
      tap((breakpoint: Breakpoint) => this.breakpoint = breakpoint)
    ).subscribe();

    this.store.select(SELECT_USER_STATE).pipe(
      takeUntilDestroyed(),
      tap((userState: UserState) => this.username = userState?.user?.username ? userState.user.username : 'anonymous')
    ).subscribe();
  }

  openProfile(): void {
    from(this.dialogService.open(ProfileDialogComponent)).subscribe();
  }
}
