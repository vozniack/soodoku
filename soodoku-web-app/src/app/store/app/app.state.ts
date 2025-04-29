export interface SoodokuState {
  theme: string;
  user: UserState;
}

export interface UserState {
  token?: string;
}
