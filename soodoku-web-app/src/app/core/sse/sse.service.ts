import { Injectable, OnDestroy } from '@angular/core';
import { Store } from '@ngrx/store';
import { Subscription } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ACTION_SHOW_SNACKBAR } from '../../store/app/app.actions';
import { SELECT_AUTH_STATE } from '../../store/app/auth/auth.selectors';
import { AuthState } from '../../store/app/auth/auth.state';
import {
  ACTION_FRIEND_INVITATION_ACCEPTED,
  ACTION_FRIEND_INVITATION_RECEIVED,
  ACTION_FRIEND_INVITATION_REJECTED,
  ACTION_FRIEND_INVITATION_REMOVED,
  ACTION_FRIEND_REMOVED
} from '../../store/app/friend/friend.actions';

@Injectable({
  providedIn: 'root'
})
export class SseService implements OnDestroy {

  private eventSource?: EventSource;
  private authSub: Subscription;

  baseUrl = `${environment.backendUrl}/api/notifications/sse`;

  constructor(private store: Store) {
    this.authSub = this.store.select(SELECT_AUTH_STATE).subscribe(auth => {
      this.handleAuthChange(auth);
    });
  }

  ngOnDestroy(): void {
    this.closeStream();
    this.authSub.unsubscribe();
  }

  private handleAuthChange(auth: AuthState): void {
    if (auth?.accessToken) {
      this.openStream(auth.accessToken);
    } else {
      this.closeStream();
    }
  }

  private openStream(token: string): void {
    if (this.eventSource) {
      this.closeStream();
    }

    this.eventSource = new EventSource(`${this.baseUrl}?token=${token}`);

    this.handleFriendInvitationReceivedEvent();
    this.handleFriendInvitationAcceptedEvent();
    this.handleFriendInvitationRejectedEvent();
    this.handleFriendInvitationRemovedEvent();

    this.handleFriendRemovedEvent();

    this.eventSource.onerror = (error) => {
      console.error('SSE connection error', error);
    };
  }

  private closeStream(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = undefined;
    }
  }

  private handleFriendInvitationReceivedEvent(): void {
    this.eventSource!.addEventListener('FRIEND_INVITATION_RECEIVED', (event: MessageEvent) => {
      let payload = JSON.parse(event.data);

      this.store.dispatch(ACTION_FRIEND_INVITATION_RECEIVED({invitationId: payload.invitationId}));
      this.store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${payload.senderUsername} has invited you to friends!`,
        icon: 'notifications'
      }));
    });
  }

  private handleFriendInvitationAcceptedEvent(): void {
    this.eventSource!.addEventListener('FRIEND_INVITATION_ACCEPTED', (event: MessageEvent) => {
      let payload = JSON.parse(event.data);

      this.store.dispatch(ACTION_FRIEND_INVITATION_ACCEPTED({invitationId: payload.invitationId}));
      this.store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${payload.receiverUsername} has accepted your friend invitation!`,
        icon: 'notifications'
      }));
    });
  }

  private handleFriendInvitationRejectedEvent(): void {
    this.eventSource!.addEventListener('FRIEND_INVITATION_REJECTED', (event: MessageEvent) => {
      let payload = JSON.parse(event.data);

      this.store.dispatch(ACTION_FRIEND_INVITATION_REJECTED({invitationId: payload.invitationId}));
      this.store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${payload.receiverUsername} has rejected your friend invitation!`,
        icon: 'notifications'
      }));
    });
  }

  private handleFriendInvitationRemovedEvent(): void {
    this.eventSource!.addEventListener('FRIEND_INVITATION_REMOVED', (event: MessageEvent) => {
      let payload = JSON.parse(event.data);

      this.store.dispatch(ACTION_FRIEND_INVITATION_REMOVED({invitationId: payload.invitationId}));
      this.store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${payload.senderUsername} has withdrawn friend invitation!`,
        icon: 'notifications'
      }));
    });
  }

  private handleFriendRemovedEvent(): void {
    this.eventSource!.addEventListener('FRIEND_REMOVED', (event: MessageEvent) => {
      let payload = JSON.parse(event.data);

      this.store.dispatch(ACTION_FRIEND_REMOVED({friendUsername: payload.friendUsername}));
      this.store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${payload.friendUsername} has removed a friendship!`,
        icon: 'notifications'
      }));
    });
  }
}
