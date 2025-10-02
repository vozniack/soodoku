import { AsyncPipe, NgForOf, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Actions, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { BehaviorSubject, combineLatest, debounceTime, filter, Subject } from 'rxjs';
import { map, takeUntil, tap } from 'rxjs/operators';
import { ACTION_SHOW_SNACKBAR } from '../../../../store/app/app.actions';
import { ACTION_FRIEND_INVITATION_ACCEPTED, ACTION_FRIEND_REMOVED } from '../../../../store/app/friend/friend.actions';
import { fadeInAnimation } from '../../../animations/fade-in-animation';
import { ButtonComponent } from '../../../components/button/button.component';
import { DividerComponent } from '../../../components/divider/divider.component';
import { InputComponent } from '../../../components/input/input.component';
import { UserComponent } from '../../../components/user/user.component';
import { FriendService } from '../social-dialog.service';
import { Friend } from './social-friends.interface';

@Component({
  selector: 'soo-social-friends',
  standalone: true,
  imports: [DividerComponent, InputComponent, UserComponent, ButtonComponent, NgForOf, NgIf, AsyncPipe],
  templateUrl: './social-friends.component.html',
  styleUrl: './social-friends.component.scss',
  animations: [fadeInAnimation]
})
export class SocialFriendsComponent implements OnInit {

  active?: Friend;

  editMode = false;

  private destroy$ = new Subject<void>();

  private friends$ = new BehaviorSubject<Friend[]>([]);
  private search$ = new BehaviorSubject<string>('');

  constructor(private friendService: FriendService, private store: Store, private actions$: Actions) {
  }

  ngOnInit(): void {
    this.getFriends();

    this.actions$.pipe(
      ofType(ACTION_FRIEND_INVITATION_ACCEPTED),
      takeUntil(this.destroy$),
      tap(() => this.getFriends())
    ).subscribe();

    this.actions$.pipe(
      ofType(ACTION_FRIEND_REMOVED),
      takeUntil(this.destroy$),
      filter(action => !!action.friendUsername),
      tap((action) => {
        this.friends$.next(this.friends$.value.filter(
          friend => friend.friend.username !== action.friendUsername
        ));
      })
    ).subscribe();
  }

  filteredFriends$ = combineLatest([this.friends$, this.search$]).pipe(
    debounceTime(64),
    map(([friends, term]) =>
      friends.filter(f =>
        f.friend.username.toLowerCase().includes(term.toLowerCase())
      )
    )
  );

  onSearch(event: Event) {
    this.search$.next((event.target as HTMLInputElement).value);
    this.active = undefined;
  }

  switchEditMode(): void {
    this.editMode = !this.editMode;

    if (!this.editMode) {
      this.active = undefined;
    }
  }

  setActive(friend: Friend): void {
    this.active = friend;
  }

  remove(): void {
    if (this.active != undefined) {
      this.friendService.removeFriend(this.active!.id).pipe(
        tap(() => {
          this.store.dispatch(ACTION_SHOW_SNACKBAR({message: 'Friendship ended', icon: 'waving_hand'}));

          this.friends$.next(this.friends$.value.filter(f => f.id !== this.active!!.id));
          this.active = undefined;
        })
      ).subscribe();
    }
  }

  clear(): void {
    this.active = undefined;
  }

  private getFriends(): void {
    this.friendService.getFriends().pipe(
      tap((response: Friend[]) => this.friends$.next(response))
    ).subscribe();
  }
}
