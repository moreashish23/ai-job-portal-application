import { Navigate } from 'react-router-dom';
import { useAuth } from '@/hooks/useAuth';
import type { UserRole } from '@/types/user.types';
import { ROUTES } from '@/constants/routes';

interface RoleRouteProps {
  children: React.ReactNode;
  allowedRoles: UserRole[];
}

export function RoleRoute({ children, allowedRoles }: RoleRouteProps) {
  const { user, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to={ROUTES.LOGIN} replace />;
  }

  if (!user) {
    return <Navigate to={ROUTES.LOGIN} replace />;
  }

  if (!allowedRoles.includes(user.role)) {
    const redirectMap: Record<UserRole, string> = {
      ROLE_JOB_SEEKER: ROUTES.CANDIDATE.DASHBOARD,
      ROLE_EMPLOYER: ROUTES.RECRUITER.DASHBOARD,
      ROLE_ADMIN: ROUTES.ADMIN.DASHBOARD,
    };

    return <Navigate to={redirectMap[user.role]} replace />;
  }

  return <>{children}</>;
}