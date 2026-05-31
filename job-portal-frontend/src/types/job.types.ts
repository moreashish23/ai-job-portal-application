export type JobType =
  | 'FULL_TIME' | 'PART_TIME' | 'CONTRACT'
  | 'INTERNSHIP' | 'FREELANCE' | 'REMOTE';

export type JobStatus = 'DRAFT' | 'OPEN' | 'CLOSED' | 'EXPIRED' | 'FILLED';

export type WorkMode = 'REMOTE' | 'HYBRID' | 'ONSITE';

export type ExperienceLevel =
  | 'ENTRY_LEVEL' | 'JUNIOR' | 'MID_LEVEL'
  | 'SENIOR_LEVEL' | 'LEAD' | 'EXECUTIVE';

export type SkillCategory =
  | 'PROGRAMMING_LANGUAGE' | 'FRAMEWORK' | 'DATABASE' | 'CLOUD_PLATFORM'
  | 'DEVOPS' | 'DESIGN' | 'SOFT_SKILL' | 'TOOL' | 'LANGUAGE' | 'OTHER';

export interface JobCategoryResponse {
  id: number;
  name: string;
  slug: string;
  description: string | null;
  iconUrl: string | null;
  active: boolean;
  parentId: number | null;
  parentName: string | null;
  subCategories: JobCategoryResponse[];
  createdAt: string;
}

export interface JobSkillResponse {
  id: number;
  name: string;
  slug: string;
  category: SkillCategory;
  active: boolean;
}

export interface JobTagResponse {
  id: number;
  name: string;
  slug: string;
}

export interface JobResponse {
  id: number;
  title: string;
  description: string;
  requirements: string | null;
  responsibilities: string | null;
  benefits: string | null;
  company: import('./company.types').CompanyResponse | null;
  companyId: number;
  employerId: number;
  category: JobCategoryResponse | null;
  skills: JobSkillResponse[];
  tags: JobTagResponse[];
 
  address: string | null;
  city: string | null;
  state: string | null;
  country: string | null;
  zipCode: string | null;

  minSalary: number | null;
  maxSalary: number | null;
  
  jobType: JobType;
  workMode: WorkMode;
  experienceLevel: ExperienceLevel;
  status: JobStatus;

  openings: number;
  applicationDeadline: string | null;
  expiresAt: string | null;
  active: boolean;
 
  createdAt: string;
  updatedAt: string;
  publishedAt: string | null;
  closedAt: string | null;
}

export interface JobRequest {
  title: string;
  description: string;
  requirements?: string;
  responsibilities?: string;
  benefits?: string;
  companyId: number;
  categoryId: number;
  skillIds?: number[];
  tagIds?: number[];
  address?: string;
  city?: string;
  state?: string;
  country?: string;
  zipCode?: string;
  minSalary?: number;
  maxSalary?: number;
  jobType: JobType;
  workMode: WorkMode;
  experienceLevel: ExperienceLevel;
  openings?: number;
  applicationDeadline?: string;
  expiresAt?: string;
}

export interface JobSearchParams {
  keyword?: string;
  location?: string;
  categoryId?: number;
  skillIds?: number[];
  tagIds?: number[];
  jobType?: JobType;
  workMode?: WorkMode;
  experienceLevel?: ExperienceLevel;
  minSalary?: number;
  maxSalary?: number;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: 'asc' | 'desc';
}