import type { UserRole, UserStatus } from './user.types';

export interface SignupRequest {
  fullName: string;
  email: string;
  password: string;
  phone?: string;
  role: UserRole;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  jwt: string;
  title: string;
  message: string;
  user: UserResponse;
}


import type { UserResponse } from './user.types';
export type { UserResponse };