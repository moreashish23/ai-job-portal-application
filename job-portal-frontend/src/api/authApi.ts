import apiClient from './axios';
import type { AuthResponse, SignupRequest, LoginRequest } from '@/types/auth.types';

export const authApi = {
  signup: (data: SignupRequest) =>
    apiClient.post<AuthResponse>('/auth/signup', data),

  login: (data: LoginRequest) =>
    apiClient.post<AuthResponse>('/auth/login', data),
};