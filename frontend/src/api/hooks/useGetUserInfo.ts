import { useQuery } from '@tanstack/react-query';
import { AxiosError, isAxiosError } from 'axios';
import { fetchInstance } from '../instance';
import { UserInfoData } from '@/types';

export const getUserInfoPath = () => `/users/info`;

interface ApiErrorResponse {
  message: string;
  code: string;
  status: number;
}

export const isAuthorizationError = (
  error: unknown,
): error is AxiosError<ApiErrorResponse> & {
  response: NonNullable<AxiosError<ApiErrorResponse>['response']>;
} => {
  if (!isAxiosError(error)) return false;
  if (!error.response) return false;

  return (
    error.response.status === 401 && error.response.data !== undefined && typeof error.response.data.code === 'string'
  );
};

interface QueryOptions {
  retry?: boolean | number;
  enabled?: boolean;
  onError?: (error: unknown) => void;
  onSuccess?: (data: UserInfoData) => void;
}

export const getUserInfo = async () => {
  try {
    const response = await fetchInstance.get<UserInfoData>(getUserInfoPath(), {
      withCredentials: true,
    });
    return response.data;
  } catch (error) {
    console.error('Failed to fetch user info:', error);
    throw error;
  }
};

export const useGetUserInfo = (options: QueryOptions = {}) => {
  return useQuery<UserInfoData, unknown>({
    queryKey: ['UserInfo'],
    queryFn: () => getUserInfo(),
    retry: false,
    staleTime: 5 * 60 * 1000,
    ...options,
  });
};
