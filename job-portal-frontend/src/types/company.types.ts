export type CompanySize = 'MICRO' | 'SMALL' | 'MEDIUM' | 'LARGE' | 'ENTERPRISE';

export type CompanyType =
  | 'STARTUP' | 'PRIVATE' | 'PUBLIC_LISTED'
  | 'GOVERNMENT' | 'NON_PROFIT' | 'EDUCATIONAL' | 'SELF_EMPLOYED';

export type CompanyStatus =
  | 'PENDING_VERIFICATION' | 'ACTIVE' | 'SUSPENDED' | 'REJECTED';

export type IndustryType =
  | 'TECHNOLOGY' | 'FINANCE_BANKING' | 'HEALTHCARE' | 'EDUCATION'
  | 'MANUFACTURING' | 'RETAIL_ECOMMERCE' | 'HOSPITALITY_TOURISM'
  | 'REAL_ESTATE' | 'MEDIA_ENTERTAINMENT' | 'TRANSPORTATION_LOGISTICS'
  | 'ENERGY_UTILITIES' | 'AGRICULTURE' | 'CONSULTING' | 'LEGAL'
  | 'TELECOMMUNICATIONS' | 'AUTOMOTIVE' | 'PHARMACEUTICAL'
  | 'CONSTRUCTION' | 'HUMAN_RESOURCES' | 'MARKETING_ADVERTISING' | 'OTHER';

export type SocialPlatform =
  | 'LINKEDIN' | 'TWITTER' | 'FACEBOOK' | 'GITHUB'
  | 'INSTAGRAM' | 'YOUTUBE' | 'WEBSITE';

export interface SocialLink {
  platform: SocialPlatform;
  url: string;
}

export interface CompanyResponse {
  id: number;
  name: string;
  slug: string;
  tagline: string | null;
  description: string | null;
  logoUrl: string | null;
  coverImageUrl: string | null;
  website: string | null;
  email: string | null;
  phone: string | null;
  foundedYear: number | null;
  companySize: CompanySize | null;
  companyType: CompanyType | null;
  industryType: IndustryType | null;
  status: CompanyStatus;
  active: boolean | null;
  ownerId: number;
  socialLinks: SocialLink[];
  createdAt: string;
  updatedAt: string;
  verifiedAt: string | null;
}

export interface CompanyRequest {
  name: string;
  tagline?: string;
  description?: string;
  logoUrl?: string;
  coverImageUrl?: string;
  website?: string;
  email?: string;
  phone?: string;
  foundedYear?: number;
  companySize: CompanySize;
  companyType: CompanyType;
  industryType: IndustryType;
  registrationNumber?: string;
  socialLinks?: SocialLink[];
}