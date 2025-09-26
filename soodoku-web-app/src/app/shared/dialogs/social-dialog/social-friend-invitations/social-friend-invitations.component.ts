import { DatePipe, NgForOf, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Actions, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { Subject } from 'rxjs';
import { takeUntil, tap } from 'rxjs/operators';
import { ACTION_INVITATION_ACCEPTED, ACTION_INVITATION_SENT } from '../../../../store/app/friend/friend.actions';
import { fadeInAnimation } from '../../../animations/fade-in-animation';
import { ButtonComponent } from '../../../components/button/button.component';
import { DividerComponent } from '../../../components/divider/divider.component';
import { IconComponent } from '../../../components/icon/icon.component';
import { FriendService } from '../social-dialog.service';
import { FriendInvitation } from './social-friend-invitations.interface';

@Component({
  selector: 'soo-social-friend-invitations',
  standalone: true,
  imports: [DividerComponent, ButtonComponent, NgForOf, NgIf, IconComponent, DatePipe],
  templateUrl: './social-friend-invitations.component.html',
  styleUrl: './social-friend-invitations.component.scss',
  animations: [fadeInAnimation]
})
export class SocialFriendInvitationsComponent implements OnInit {

  sentInvitations: FriendInvitation[] = [];
  receivedInvitations: FriendInvitation[] = [];

  private destroy$ = new Subject<void>();

  constructor(private friendService: FriendService, private store: Store, private actions$: Actions) {
  }

  ngOnInit() {
    this.loadReceivedInvitations();
    this.loadSentInvitations();

    this.actions$.pipe(
      ofType(ACTION_INVITATION_SENT),
      takeUntil(this.destroy$),
      tap(() => this.loadSentInvitations())
    ).subscribe();
  }

  accept(id: string) {
    this.friendService.accept(id).pipe(
      tap(() => {
        this.store.dispatch(ACTION_INVITATION_ACCEPTED());

        this.receivedInvitations.splice(
          this.receivedInvitations.findIndex(i => i.id === id), 1
        );
      })
    ).subscribe();
  }

  reject(id: string) {
    this.friendService.reject(id).pipe(
      tap(() => this.receivedInvitations.splice(
        this.receivedInvitations.findIndex(i => i.id === id), 1
      ))
    ).subscribe();
  }

  delete(id: string) {
    this.friendService.delete(id).pipe(
      tap(() => this.sentInvitations = this.sentInvitations.filter(inv => inv.id !== id))
    ).subscribe();
  }

  private loadSentInvitations(): void {
    this.friendService.getSent().pipe(
      tap((response: FriendInvitation[]) => this.sentInvitations = response)
    ).subscribe();
  }

  private loadReceivedInvitations(): void {
    this.friendService.getReceived().pipe(
      tap((response: FriendInvitation[]) => this.receivedInvitations = response)
    ).subscribe();
  }
}
