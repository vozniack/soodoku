import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { tap } from 'rxjs/operators';
import { DialogService } from '../../../core/dialog/dialog.service';
import { SELECT_USER_STATE } from '../../../store/app/user/user.selector';
import { UserState } from '../../../store/app/user/user.state';
import { ProfileDialogComponent } from '../../dialogs/profile-dialog/profile-dialog.component';
import { IconComponent } from '../icon/icon.component';

@Component({
  selector: 'soo-avatar',
  standalone: true,
  imports: [IconComponent],
  templateUrl: './avatar.component.html',
  styleUrl: './avatar.component.scss'
})
export class AvatarComponent {

  username!: string;

  constructor(private store: Store, private dialogService: DialogService) {
    this.store.select(SELECT_USER_STATE).pipe(
      tap((userState: UserState) => this.username = userState?.user?.username ? userState.user.username : 'anonymous')
    ).subscribe();
  }

  openProfile(): void {
    from(this.dialogService.open(ProfileDialogComponent)).subscribe();
  }
}
