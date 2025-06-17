export interface User {
  id: string,

  email: string,
  username: string,
  language: string,
  theme: string
}

export interface UserUpdateUsernameRequest {
  username: string;
}

export interface UserUpdatePasswordRequest {
  password: string;
}

export interface UserUpdateLanguageRequest {
  language: string;
}

export interface UserUpdateThemeRequest {
  theme: string;
}
