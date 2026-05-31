export const ROUTES = {
  
  HOME: '/',
  ABOUT: '/about',
  CONTACT: '/contact',
  JOBS: '/jobs',
  JOB_DETAIL: '/jobs/:id',
  COMPANIES: '/companies',
  COMPANY_DETAIL: '/companies/:slug',
  SEARCH: '/search',
  PRIVACY: '/privacy',
  TERMS: '/terms',
  NOT_FOUND: '*',

 
  LOGIN: '/login',
  REGISTER: '/register',
  FORGOT_PASSWORD: '/forgot-password',

  
  CANDIDATE: {
    ROOT: '/candidate',
    DASHBOARD: '/candidate/dashboard',
    PROFILE: '/candidate/profile',
    RESUME: '/candidate/resume',
    APPLICATIONS: '/candidate/applications',
    SAVED_JOBS: '/candidate/saved-jobs',
    ALERTS: '/candidate/alerts',
    SETTINGS: '/candidate/settings',
  },

  
  RECRUITER: {
    ROOT: '/recruiter',
    DASHBOARD: '/recruiter/dashboard',
    COMPANY: '/recruiter/company',
    JOBS: '/recruiter/jobs',
    POST_JOB: '/recruiter/jobs/new',
    EDIT_JOB: '/recruiter/jobs/:id/edit',
    APPLICATIONS: '/recruiter/applications',
    JOB_APPLICATIONS: '/recruiter/jobs/:id/applications',
    APPLICATION_DETAIL: '/recruiter/applications/:id',
    SETTINGS: '/recruiter/settings',
  },


  ADMIN: {
    ROOT: '/admin',
    DASHBOARD: '/admin/dashboard',
    USERS: '/admin/users',
    COMPANIES: '/admin/companies',
    JOBS: '/admin/jobs',
    SETTINGS: '/admin/settings',
  },
} as const;