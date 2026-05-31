const TOKEN_KEY = 'jp_access_token';
const THEME_KEY = 'jp_theme';

export const storage = {
  getToken: (): string | null => localStorage.getItem(TOKEN_KEY),
  setToken: (token: string): void => localStorage.setItem(TOKEN_KEY, token),
  removeToken: (): void => localStorage.removeItem(TOKEN_KEY),

  getTheme: (): 'light' | 'dark' => {
    return (localStorage.getItem(THEME_KEY) as 'light' | 'dark') ?? 'light';
  },
  setTheme: (theme: 'light' | 'dark'): void => localStorage.setItem(THEME_KEY, theme),
};