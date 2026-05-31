import type { JobType, WorkMode, ExperienceLevel } from './job.types';

export interface GenerateJobDescriptionRequest {
  jobTitle: string;
  companyName?: string;
  jobType?: JobType;
  workMode?: WorkMode;
  experienceLevel?: ExperienceLevel;
  requiredSkills?: string[];
  additionalContext?: string;
}

export interface GenerateCoverLetterRequest {
  jobTitle: string;
  companyName: string;
  candidateName: string;
  candidateExperienceSummary: string;
  candidateSkills?: string[];
  tone?: string;
}

export interface OptimizeResumeRequest {
  currentSummary: string;
  currentSkills?: string[];
  targetJobTitle?: string;
  targetExperienceLevel?: string;
}

export interface NaturalLanguageJobSearchRequest {
  query: string;
}

export interface AiTextResponse {
  result: string;
  success: boolean;
  model: string;
}

export interface CandidateScoreResponse {
  applicationId: number;
  score: number;
  summary: string;
  matchedSkills: string;
  missingSkills: string;
  recommendation: string;
  success: boolean;
}

export interface JobSearchQueryResponse {
  originalQuery: string;
  keyword: string | null;
  location: string | null;
  jobType: JobType | null;
  workMode: WorkMode | null;
  experienceLevel: ExperienceLevel | null;
  success: boolean;
}