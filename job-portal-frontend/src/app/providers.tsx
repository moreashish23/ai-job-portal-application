import { Provider as ReduxProvider } from 'react-redux';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { Toaster } from 'sonner';
import { store } from '@/store';
import { useEffect } from 'react';
import { logout } from '@/store/authSlice';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 60 * 2,       
      gcTime: 1000 * 60 * 10,         
      retry: (failureCount, error: unknown) => {
      
        const status = (error as { response?: { status?: number } })?.response?.status;
        if (status === 401 || status === 403 || status === 404) return false;
        return failureCount < 2;
      },
      refetchOnWindowFocus: false,
    },
    mutations: {
      retry: false,
    },
  },
});


function UnauthorizedListener() {
  const dispatch = store.dispatch;
  useEffect(() => {
    const handler = () => {
      dispatch(logout());
      queryClient.clear();
    };
    window.addEventListener('jp:unauthorized', handler);
    return () => window.removeEventListener('jp:unauthorized', handler);
  }, [dispatch]);
  return null;
}

interface ProvidersProps {
  children: React.ReactNode;
}

export function Providers({ children }: ProvidersProps) {
  return (
    <ReduxProvider store={store}>
      <QueryClientProvider client={queryClient}>
        <UnauthorizedListener />
        {children}
        <Toaster
          position="top-right"
          richColors
          closeButton
          duration={4000}
          toastOptions={{
            style: { fontFamily: 'Inter, sans-serif' },
          }}
        />
        {import.meta.env.DEV && (
          <ReactQueryDevtools initialIsOpen={false} buttonPosition="bottom-right" />
        )}
      </QueryClientProvider>
    </ReduxProvider>
  );
}