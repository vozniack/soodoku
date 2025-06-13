export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
}

export interface SignupRequest {
  email: string;
  password: string;
  username: string;
}

export interface SignupResponse {
  token: string;
}
