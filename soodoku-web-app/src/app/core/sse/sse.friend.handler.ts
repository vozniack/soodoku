import { Store } from '@ngrx/store';
import {
  ACTION_FRIEND_INVITATION_RECEIVED,
  ACTION_FRIEND_INVITATION_ACCEPTED,
  ACTION_FRIEND_INVITATION_REJECTED,
  ACTION_FRIEND_INVITATION_REMOVED,
  ACTION_FRIEND_REMOVED
} from '../../store/app/friend/friend.actions';
import { ACTION_SHOW_SNACKBAR } from '../../store/app/app.actions';

export function registerFriendHandlers(
  sseService: { on<T>(eventName: string, handler: (payload: T) => void): void }, store: Store
): void {
  sseService.on<{ invitationId: string; senderUsername: string }>(
    'FRIEND_INVITATION_RECEIVED',
    (p) => {
      store.dispatch(ACTION_FRIEND_INVITATION_RECEIVED({invitationId: p.invitationId}));
      store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${p.senderUsername} has invited you to friends!`,
        icon: 'notifications'
      }));
    }
  );

  sseService.on<{ invitationId: string; receiverUsername: string }>(
    'FRIEND_INVITATION_ACCEPTED',
    (p) => {
      store.dispatch(ACTION_FRIEND_INVITATION_ACCEPTED({invitationId: p.invitationId}));
      store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${p.receiverUsername} has accepted your friend invitation!`,
        icon: 'notifications'
      }));
    }
  );

  sseService.on<{ invitationId: string; receiverUsername: string }>(
    'FRIEND_INVITATION_REJECTED',
    (p) => {
      store.dispatch(ACTION_FRIEND_INVITATION_REJECTED({invitationId: p.invitationId}));
      store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${p.receiverUsername} has rejected your friend invitation!`,
        icon: 'notifications'
      }));
    }
  );

  sseService.on<{ invitationId: string; senderUsername: string }>(
    'FRIEND_INVITATION_REMOVED',
    (p) => {
      store.dispatch(ACTION_FRIEND_INVITATION_REMOVED({invitationId: p.invitationId}));
      store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${p.senderUsername} has withdrawn friend invitation!`,
        icon: 'notifications'
      }));
    }
  );

  sseService.on<{ friendUsername: string }>(
    'FRIEND_REMOVED',
    (p) => {
      store.dispatch(ACTION_FRIEND_REMOVED({friendUsername: p.friendUsername}));
      store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${p.friendUsername} has removed a friendship!`,
        icon: 'notifications'
      }));
    }
  );
}
