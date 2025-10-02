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
    (payload) => {
      store.dispatch(ACTION_FRIEND_INVITATION_RECEIVED({invitationId: payload.invitationId}));
      store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${payload.senderUsername} has invited you to friends!`,
        icon: 'notifications'
      }));
    }
  );

  sseService.on<{ invitationId: string; receiverUsername: string }>(
    'FRIEND_INVITATION_ACCEPTED',
    (payload) => {
      store.dispatch(ACTION_FRIEND_INVITATION_ACCEPTED({invitationId: payload.invitationId}));
      store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${payload.receiverUsername} has accepted your friend invitation!`,
        icon: 'notifications'
      }));
    }
  );

  sseService.on<{ invitationId: string; receiverUsername: string }>(
    'FRIEND_INVITATION_REJECTED',
    (payload) => {
      store.dispatch(ACTION_FRIEND_INVITATION_REJECTED({invitationId: payload.invitationId}));
      store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${payload.receiverUsername} has rejected your friend invitation!`,
        icon: 'notifications'
      }));
    }
  );

  sseService.on<{ invitationId: string; senderUsername: string }>(
    'FRIEND_INVITATION_REMOVED',
    (payload) => {
      store.dispatch(ACTION_FRIEND_INVITATION_REMOVED({invitationId: payload.invitationId}));
      store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${payload.senderUsername} has withdrawn friend invitation!`,
        icon: 'notifications'
      }));
    }
  );

  sseService.on<{ friendUsername: string }>(
    'FRIEND_REMOVED',
    (payload) => {
      store.dispatch(ACTION_FRIEND_REMOVED({friendUsername: payload.friendUsername}));
      store.dispatch(ACTION_SHOW_SNACKBAR({
        message: `${payload.friendUsername} has removed a friendship!`,
        icon: 'notifications'
      }));
    }
  );
}
