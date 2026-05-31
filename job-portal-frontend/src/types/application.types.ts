import type { JobResponse } from './job.types';
import type { ResumeResponse } from './resume.types';

export type ApplicationStatus =
  | 'PENDING' | 'REVIEWED' | 'SHORTLISTED'
  | 'INTERVIEW_SCHEDULED' | 'OFFER_EXTENDED'
  | 'HIRED' | 'REJECTED' | 'WITHDRAWN';

export interface ApplyJobRequest {
  jobId: number;
  resumeId: number;
  coverLetter?: string;
  additionalAnswers?: string;
}

export interface ApplicationResponse {
  id: number;
  candidateId: number;
  jobId: number;
  employerId: number;
  companyId: number;
  resumeId: number;
  status: ApplicationStatus;
  coverLetter: string | null;
  aiScore: number | null;
  aiScreeningSummary: string | null;
  viewed: boolean;
  shortlisted: boolean;
  additionalAnswers: string | null;
  appliedAt: string;
  updatedAt: string;
  job: JobResponse | null;
  resume: ResumeResponse | null;
}

export interface ApplicationSummaryResponse {
  id: number;
  candidateId: number;
  jobId: number;
  companyId: number;
  status: ApplicationStatus;
  viewed: boolean;
  shortlisted: boolean;
  aiScore: number | null;
  appliedAt: string;
  updatedAt: string;
}

export interface ApplicationNoteResponse {
  id: number;
  applicationId: number;
  authorId: number;
  content: string;
  createdAt: string;
  updatedAt: string;
}