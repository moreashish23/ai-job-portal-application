export const QUERY_KEYS = {

  ME: ['me'] as const,

 
  USERS: ['users'] as const,
  USER: (id: number) => ['users', id] as const,


  JOBS: (params?: object) => ['jobs', params] as const,
  JOB: (id: number) => ['jobs', id] as const,
  JOB_CATEGORIES: ['job-categories'] as const,
  JOB_SKILLS: ['job-skills'] as const,
  JOB_TAGS: ['job-tags'] as const,
  COMPANY_JOBS: (companyId: number) => ['jobs', 'company', companyId] as const,
  RECRUITER_JOBS: ['jobs', 'recruiter'] as const,


  COMPANIES: (params?: object) => ['companies', params] as const,
  COMPANY: (id: number) => ['companies', id] as const,
  MY_COMPANY: ['companies', 'my'] as const,


  MY_RESUMES: ['resumes', 'my'] as const,
  RESUME: (id: number) => ['resumes', id] as const,


  MY_APPLICATIONS: ['applications', 'my'] as const,
  JOB_APPLICATIONS: (jobId: number) => ['applications', 'job', jobId] as const,
  APPLICATION: (id: number) => ['applications', id] as const,
  APPLICATION_NOTES: (id: number) => ['applications', id, 'notes'] as const,

  
  SAVED_JOBS: ['saved-jobs'] as const,
  IS_JOB_SAVED: (jobId: number) => ['saved-jobs', 'check', jobId] as const,
  JOB_ALERTS: ['job-alerts'] as const,


  AI_JOB_DESCRIPTION: ['ai', 'job-description'] as const,
} as const;