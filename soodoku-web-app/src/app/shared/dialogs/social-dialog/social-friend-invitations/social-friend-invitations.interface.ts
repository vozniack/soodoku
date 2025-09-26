import { UserSimple } from '../../../../core/user/user.interface';

export interface FriendInvitation {
  id: string;

  sender: UserSimple;
  receiver: UserSimple;

  createdAt: string;
  respondedAt?: string;
}
