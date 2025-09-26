import { createAction } from '@ngrx/store';

export const ACTION_INVITATION_SENT = createAction(
  '[Friend] Invitation sent'
);

export const ACTION_INVITATION_ACCEPTED = createAction(
  '[Friend] Invitation accepted'
);
