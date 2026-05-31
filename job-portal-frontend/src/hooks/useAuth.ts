import { useAppSelector, useAppDispatch } from '@/store';
import { logout } from '@/store/authSlice';
import type { UserRole } from '@/types/user.types';

export function useAuth() {
  const dispatch = useAppDispatch();
  const { user, token, isAuthenticated, isLoading } = useAppSelector(
    (state) => state.auth
  );

  const isCandidate = user?.role === 'ROLE_JOB_SEEKER';
  const isRecruiter = user?.role === 'ROLE_EMPLOYER';
  const isAdmin = user?.role === 'ROLE_ADMIN';

  const hasRole = (role: UserRole) => user?.role === role;

  const signOut = () => dispatch(logout());

  return {
    user,
    token,
    isAuthenticated,
    isLoading,
    isCandidate,
    isRecruiter,
    isAdmin,
    hasRole,
    signOut,
  };
}