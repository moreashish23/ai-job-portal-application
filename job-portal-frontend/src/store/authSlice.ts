import { createSlice, type PayloadAction } from '@reduxjs/toolkit';
import type { UserResponse } from '@/types/user.types';
import { storage } from '@/utils/storage';

interface AuthState {
  user: UserResponse | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
}

const initialState: AuthState = {
  user: null,
  token: storage.getToken(),
  isAuthenticated: !!storage.getToken(),
  isLoading: false,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials: (
      state,
      action: PayloadAction<{ user: UserResponse; token: string }>
    ) => {
      state.user = action.payload.user;
      state.token = action.payload.token;
      state.isAuthenticated = true;
      storage.setToken(action.payload.token);
    },
    updateUser: (state, action: PayloadAction<UserResponse>) => {
      state.user = action.payload;
    },
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.isAuthenticated = false;
      storage.removeToken();
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },
  },
});

export const { setCredentials, updateUser, logout, setLoading } = authSlice.actions;
export default authSlice.reducer;