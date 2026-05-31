
export interface ApiResponse {
  message: string;
  status: boolean;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;       
  size: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}


export type ValidationErrors = Record<string, string>;


export type ApiError = ApiResponse | ValidationErrors;