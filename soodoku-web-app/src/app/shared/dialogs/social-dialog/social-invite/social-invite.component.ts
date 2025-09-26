import { AsyncPipe, NgForOf } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Store } from '@ngrx/store';
import { debounceTime, forkJoin, of } from 'rxjs';
import { distinctUntilChanged, switchMap } from 'rxjs/operators';
import { UserSimple } from '../../../../core/user/user.interface';
import { ACTION_SHOW_SNACKBAR } from '../../../../store/app/app.actions';
import { ACTION_INVITATION_SENT } from '../../../../store/app/friend/friend.actions';
import { fadeInAnimation } from '../../../animations/fade-in-animation';
import { ButtonComponent } from '../../../components/button/button.component';
import { DividerComponent } from '../../../components/divider/divider.component';
import { InputComponent } from '../../../components/input/input.component';
import { UserComponent } from '../../../components/user/user.component';
import { FriendService } from '../social-dialog.service';

@Component({
  selector: 'soo-social-invite',
  standalone: true,
  imports: [ButtonComponent, DividerComponent, InputComponent, AsyncPipe, NgForOf, UserComponent],
  templateUrl: './social-invite.component.html',
  styleUrl: './social-invite.component.scss',
  animations: [fadeInAnimation]
})
export class SocialInviteComponent {

  searchControl: FormControl = new FormControl('');
  activeCandidates: UserSimple[] = [];

  constructor(private friendService: FriendService, private store: Store) {
    this.searchControl.valueChanges.pipe(
      debounceTime(128),
      distinctUntilChanged()
    ).subscribe(() => this.activeCandidates = []);
  }

  friendCandidates$ = this.searchControl.valueChanges.pipe(
    debounceTime(128),
    distinctUntilChanged(),
    switchMap(term => {
      const trimmed = (term ?? '').trim();
      return trimmed.length > 2 ? this.friendService.getFriendCandidates(trimmed) : of([]);
    })
  );

  activate(candidate: UserSimple): void {
    const idx = this.activeCandidates.findIndex(c => c.username === candidate.username);

    if (idx === -1) {
      this.activeCandidates.push(candidate);
    } else {
      this.activeCandidates.splice(idx, 1);
    }
  }

  isActive(candidate: UserSimple): boolean {
    return this.activeCandidates.some(c => c.username === candidate.username);
  }

  invite(): void {
    if (this.activeCandidates.length === 0) {
      return;
    }

    const requests = this.activeCandidates.map(candidate =>
      this.friendService.invite(candidate.username)
    );

    forkJoin(requests).subscribe({
      next: () => {
        this.searchControl.reset();

        this.store.dispatch(ACTION_INVITATION_SENT());
        this.store.dispatch(ACTION_SHOW_SNACKBAR({message: 'Invitation sent', icon: 'forward_to_inbox'}))
      }
    });
  }
}
