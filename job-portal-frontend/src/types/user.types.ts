export type UserRole = 'ROLE_ADMIN' | 'ROLE_JOB_SEEKER' | 'ROLE_EMPLOYER';


export type UserStatus = 'ACTIVE' | 'INACTIVE' | 'SUSPENDED' | 'DELETED';


export interface UserResponse {
  id: number;
  fullName: string;
  email: string;
  phone: string | null;
  profileImage: string | null;
  role: UserRole;
  status: UserStatus;
  lastLogin: string | null;  
  createdAt: string;
}


export interface UpdateUserRequest {
  fullName?: string;
  phone?: string;
  profileImage?: string;
}