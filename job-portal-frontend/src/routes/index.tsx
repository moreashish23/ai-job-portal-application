import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { lazy, Suspense } from 'react';
import { ProtectedRoute } from './ProtectedRoute';
import { RoleRoute } from './RoleRoute';
import { ROUTES } from '@/constants/routes';


const HomePage        = lazy(() => import('@/pages/public/HomePage'));
const LoginPage       = lazy(() => import('@/pages/auth/LoginPage'));
const RegisterPage    = lazy(() => import('@/pages/auth/RegisterPage'));
const JobsPage        = lazy(() => import('@/pages/public/JobsPage'));
const JobDetailPage   = lazy(() => import('@/pages/public/JobDetailPage'));
const CompaniesPage   = lazy(() => import('@/pages/public/CompaniesPage'));
const NotFoundPage    = lazy(() => import('@/pages/public/NotFoundPage'));

const CandidateDashboard  = lazy(() => import('@/pages/candidate/DashboardPage'));
const RecruiterDashboard  = lazy(() => import('@/pages/recruiter/DashboardPage'));
const AdminDashboard      = lazy(() => import('@/pages/admin/DashboardPage'));


const PublicLayout    = lazy(() => import('@/layouts/PublicLayout'));
const CandidateLayout = lazy(() => import('@/layouts/CandidateLayout'));
const RecruiterLayout = lazy(() => import('@/layouts/RecruiterLayout'));
const AdminLayout     = lazy(() => import('@/layouts/AdminLayout'));

function PageLoader() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-surface-50 dark:bg-surface-950">
      <div className="flex flex-col items-center gap-3">
        <div className="w-8 h-8 border-4 border-primary-500 border-t-transparent rounded-full animate-spin" />
        <p className="text-sm text-surface-500">Loading…</p>
      </div>
    </div>
  );
}

const router = createBrowserRouter([
 
  {
    element: (
      <Suspense fallback={<PageLoader />}>
        <PublicLayout />
      </Suspense>
    ),
    children: [
      { path: ROUTES.HOME, element: <Suspense fallback={<PageLoader />}><HomePage /></Suspense> },
      { path: ROUTES.JOBS, element: <Suspense fallback={<PageLoader />}><JobsPage /></Suspense> },
      { path: ROUTES.JOB_DETAIL, element: <Suspense fallback={<PageLoader />}><JobDetailPage /></Suspense> },
      { path: ROUTES.COMPANIES, element: <Suspense fallback={<PageLoader />}><CompaniesPage /></Suspense> },
    ],
  },

  
  { path: ROUTES.LOGIN, element: <Suspense fallback={<PageLoader />}><LoginPage /></Suspense> },
  { path: ROUTES.REGISTER, element: <Suspense fallback={<PageLoader />}><RegisterPage /></Suspense> },

  
  {
    path: ROUTES.CANDIDATE.ROOT,
    element: (
      <ProtectedRoute>
        <RoleRoute allowedRoles={['ROLE_JOB_SEEKER']}>
          <Suspense fallback={<PageLoader />}>
            <CandidateLayout />
          </Suspense>
        </RoleRoute>
      </ProtectedRoute>
    ),
    children: [
      { path: 'dashboard', element: <Suspense fallback={<PageLoader />}><CandidateDashboard /></Suspense> },
    ],
  },

 
  {
    path: ROUTES.RECRUITER.ROOT,
    element: (
      <ProtectedRoute>
        <RoleRoute allowedRoles={['ROLE_EMPLOYER']}>
          <Suspense fallback={<PageLoader />}>
            <RecruiterLayout />
          </Suspense>
        </RoleRoute>
      </ProtectedRoute>
    ),
    children: [
      { path: 'dashboard', element: <Suspense fallback={<PageLoader />}><RecruiterDashboard /></Suspense> },
    ],
  },

 
  {
    path: ROUTES.ADMIN.ROOT,
    element: (
      <ProtectedRoute>
        <RoleRoute allowedRoles={['ROLE_ADMIN']}>
          <Suspense fallback={<PageLoader />}>
            <AdminLayout />
          </Suspense>
        </RoleRoute>
      </ProtectedRoute>
    ),
    children: [
      { path: 'dashboard', element: <Suspense fallback={<PageLoader />}><AdminDashboard /></Suspense> },
    ],
  },

  
  { path: ROUTES.NOT_FOUND, element: <Suspense fallback={<PageLoader />}><NotFoundPage /></Suspense> },
]);

export function AppRouter() {
  return <RouterProvider router={router} />;
}