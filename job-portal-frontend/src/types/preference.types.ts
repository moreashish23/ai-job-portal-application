import type { JobResponse } from './job.types';
import type { JobType, WorkMode, ExperienceLevel } from './job.types';

export type AlertFrequency = 'IMMEDIATE' | 'DAILY' | 'WEEKLY';

export interface SavedJobResponse {
  id: number;
  candidateId: number;
  jobId: number;
  note: string | null;
  savedAt: string;
  job: JobResponse | null;
}

export interface JobAlertResponse {
  id: number;
  candidateId: number;
  alertName: string;
  keyword: string | null;
  location: string | null;
  jobType: JobType | null;
  workMode: WorkMode | null;
  experienceLevel: ExperienceLevel | null;
  categoryId: number | null;
  minSalary: number | null;
  maxSalary: number | null;
  frequency: AlertFrequency;
  isActive: boolean;
  lastTriggeredAt: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateJobAlertRequest {
  alertName: string;
  keyword?: string;
  location?: string;
  jobType?: JobType;
  workMode?: WorkMode;
  experienceLevel?: ExperienceLevel;
  categoryId?: number;
  minSalary?: number;
  maxSalary?: number;
  frequency?: AlertFrequency;
}