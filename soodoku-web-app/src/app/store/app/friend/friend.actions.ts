import { createAction, props } from '@ngrx/store';

export const ACTION_FRIEND_INVITATION_SENT = createAction(
  '[Friend] Friend invitation sent'
);

export const ACTION_FRIEND_INVITATION_RECEIVED = createAction(
  '[Friend] Friend invitation received', props<{ invitationId?: string }>()
);

export const ACTION_FRIEND_INVITATION_ACCEPTED = createAction(
  '[Friend] Friend invitation accepted', props<{ invitationId?: string }>()
);

export const ACTION_FRIEND_INVITATION_REJECTED = createAction(
  '[Friend] Friend invitation rejected', props<{ invitationId?: string }>()
);

export const ACTION_FRIEND_INVITATION_REMOVED = createAction(
  '[Friend] Friend invitation removed', props<{ invitationId?: string }>()
);

export const ACTION_FRIEND_REMOVED = createAction(
  '[Friend] Friend removed', props<{ friendUsername?: string }>()
);
