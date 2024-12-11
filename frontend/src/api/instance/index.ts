import type { AxiosInstance, AxiosRequestConfig } from 'axios';
import axios from 'axios';

import { QueryClient } from '@tanstack/react-query';

const initInstance = (config: AxiosRequestConfig): AxiosInstance => {
  const instance = axios.create({
    timeout: 20000,
    ...config,
    headers: {
      'Content-Type': 'application/json',
      ...config.headers,
    },
  });
  axios.defaults.withCredentials = true;
  return instance;
};

export const BASE_URL = 'https://api.inplace.my';
export const fetchInstance = initInstance({
  baseURL: BASE_URL,
});

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
      refetchOnMount: true,
      refetchOnReconnect: true,
      refetchOnWindowFocus: true,
      throwOnError: true,
    },
    mutations: {
      throwOnError: true,
    },
  },
});
