import { UserSimple } from '../../../../core/user/user.interface';

export interface Friend {
  id: string;

  friend: UserSimple;

  since: string;
}
