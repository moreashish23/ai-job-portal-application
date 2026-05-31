import axios, {
  type AxiosError,
  type AxiosResponse,
  type InternalAxiosRequestConfig,
} from 'axios';
import { storage } from '@/utils/storage';
import { toast } from 'sonner';

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

export const apiClient = axios.create({
  baseURL: BASE_URL,
  timeout: 30_000,
  headers: {
    'Content-Type': 'application/json',
  },
});


apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = storage.getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error: AxiosError) => Promise.reject(error),
);


apiClient.interceptors.response.use(
  (response: AxiosResponse) => response,
  (error: AxiosError<{ message?: string; status?: boolean }>) => {
    const status = error.response?.status;

    
    if (status === 401) {
      storage.removeToken();
     
      window.dispatchEvent(new CustomEvent('jp:unauthorized'));
      toast.error('Session expired. Please log in again.');
    }

    
    if (status === 403) {
      toast.error('You do not have permission to perform this action.');
    }

    
    if (status === 503) {
      toast.error('Service temporarily unavailable. Please try again shortly.');
    }

   
    if (status === 500) {
      toast.error('An unexpected error occurred. Please try again.');
    }

    return Promise.reject(error);
  },
);

export default apiClient;