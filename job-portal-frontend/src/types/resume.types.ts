import type { JobType } from './job.types';

export type ResumeTemplate = 'CLASSIC' | 'MODERN' | 'CREATIVE' | 'MINIMAL' | 'PROFESSIONAL';
export type ResumeVisibility = 'PUBLIC' | 'PRIVATE' | 'LINK_ONLY';
export type ProficiencyLevel = 'BEGINNER' | 'ELEMENTARY' | 'INTERMEDIATE' | 'ADVANCED' | 'EXPERT';
export type LanguageProficiency = 'BASIC' | 'CONVERSATIONAL' | 'PROFESSIONAL' | 'FLUENT' | 'NATIVE';

export interface PersonalInfo {
  firstName: string;
  lastName: string;
  headline: string | null;
  email: string;
  phone: string | null;
  city: string | null;
  country: string | null;
  linkedInUrl: string | null;
  githubUrl: string | null;
  portfolioUrl: string | null;
  websiteUrl: string | null;
}

export interface WorkExperienceResponse {
  id: number;
  companyName: string;
  companyLogoUrl: string | null;
  jobTitle: string;
  employmentType: JobType;
  location: string | null;
  startDate: string;
  endDate: string | null;
  isCurrentJob: boolean;
  description: string | null;
  technologies: string[];
  displayOrder: number;
}

export interface EducationResponse {
  id: number;
  institutionName: string;
  degree: string;
  fieldOfStudy: string;
  grade: string | null;
  startDate: string;
  endDate: string | null;
  isCurrentlyStudying: boolean;
  description: string | null;
  displayOrder: number;
}

export interface ResumeSkillResponse {
  id: number;
  skillName: string;
  proficiencyLevel: ProficiencyLevel;
  yearsOfExperience: number | null;
  displayOrder: number;
}

export interface ProjectResponse {
  id: number;
  title: string;
  description: string | null;
  technologies: string[];
  projectUrl: string | null;
  sourceCodeUrl: string | null;
  startDate: string | null;
  endDate: string | null;
  isOngoing: boolean;
  displayOrder: number;
}

export interface LanguageResponse {
  id: number;
  languageName: string;
  proficiency: LanguageProficiency;
  displayOrder: number;
}

export interface ResumeResponse {
  id: number;
  candidateId: number;
  title: string;
  template: ResumeTemplate;
  visibility: ResumeVisibility;
  isDefault: boolean;
  personalInfo: PersonalInfo | null;
  summary: string | null;
  completionScore: number;
  createdAt: string;
  updatedAt: string;
  workExperiences: WorkExperienceResponse[];
  educations: EducationResponse[];
  skills: ResumeSkillResponse[];
  projects: ProjectResponse[];
  languages: LanguageResponse[];
}