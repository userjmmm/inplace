import { createContext, useCallback, useEffect, useMemo, useState } from 'react';
import { useGetRefreshToken } from '@/api/hooks/useGetRefreshToken';
import { useDeleteToken } from '@/api/hooks/useDeleteToken';
import { UserInfoData } from '@/types';
import { useDeleteFCMToken } from '@/api/hooks/useDeleteFCMToken';

type AuthInfo = {
  isAuthenticated: boolean;
  handleLoginSuccess: (userInfo: UserInfoData) => Promise<void>;
  handleLogout: () => void;
};

export const AuthContext = createContext<AuthInfo | undefined>(undefined);

interface AuthProviderProps {
  children: React.ReactNode;
}

const ACCESS_TOKEN_REFRESH_INTERVAL = 1 * 30 * 1000; // 30초마다 갱신

const isReactNativeWebView = typeof window !== 'undefined' && window.ReactNativeWebView != null;

export default function AuthProvider({ children }: AuthProviderProps) {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(
    () => localStorage.getItem('isAuthenticated') === 'true',
  );
  const [isInitialized, setIsInitialized] = useState<boolean>(false);
  const { mutateAsync: refreshToken } = useGetRefreshToken();
  const { mutateAsync: logout } = useDeleteToken();
  const { mutateAsync: deleteFCMToken } = useDeleteFCMToken();

  const handleLogout = useCallback(async () => {
    try {
      if (window.ReactNativeWebView) {
        window.ReactNativeWebView.postMessage(
          JSON.stringify({
            type: 'LOGOUT',
          }),
        );
        return;
      }
      await deleteFCMToken();
      await logout();
    } catch (error) {
      console.error('로그아웃 요청 실패:', error);
    } finally {
      setIsAuthenticated(false);
      localStorage.removeItem('isAuthenticated');
      localStorage.removeItem('nickname');
      window.location.href = '/';
    }
  }, [logout]);

  const refreshTokenRegularly = useCallback(async () => {
    try {
      await refreshToken();
    } catch (error) {
      console.error('Token refresh failed:', error);
      handleLogout();
    }
  }, [handleLogout]);

  const handleLoginSuccess = useCallback(
    async (userInfo: UserInfoData) => {
      if (!isAuthenticated) {
        localStorage.setItem('nickname', userInfo.nickname);
        localStorage.setItem('isAuthenticated', 'true');
        setIsAuthenticated(true);
      }
    },
    [isAuthenticated],
  );

  useEffect(() => {
    const initialize = async () => {
      const savedAuthStatus = localStorage.getItem('isAuthenticated') === 'true';

      if (savedAuthStatus && !isReactNativeWebView) {
        try {
          await refreshTokenRegularly();
        } catch (error) {
          console.error('Failed to refresh token during initialization:', error);
          handleLogout();
        }
      } else if (savedAuthStatus && isReactNativeWebView) {
        // 웹뷰 환경: 로그인 직후가 아닌 경우에만 토큰 갱신 요청
        // authToken이 있으면 로그인 직후이므로 스킵
        const hasAuthToken = localStorage.getItem('authToken');
        if (hasAuthToken) {
          setIsAuthenticated(true);
        } else {
          // authToken이 없으면 토큰 만료 가능성 → 갱신 요청
          window.ReactNativeWebView?.postMessage(
            JSON.stringify({
              type: 'REQUEST_REFRESH_TOKEN',
            }),
          );
        }
      }
      setIsInitialized(true);
    };

    initialize();
  }, [refreshTokenRegularly, handleLogout]);

  useEffect(() => {
    let intervalId: NodeJS.Timeout;

    // 웹뷰 환경에서는 자동 갱신하지 않음 (모바일 앱이 자체적으로 관리)
    if (isAuthenticated && !isReactNativeWebView) {
      intervalId = setInterval(() => {
        refreshTokenRegularly();
      }, ACCESS_TOKEN_REFRESH_INTERVAL);
    }

    return () => {
      if (intervalId) {
        clearInterval(intervalId);
      }
    };
  }, [isAuthenticated, refreshTokenRegularly]);

  const value = useMemo(
    () => (isInitialized ? { isAuthenticated, handleLoginSuccess, handleLogout } : undefined),
    [isInitialized, isAuthenticated, handleLoginSuccess, handleLogout],
  );

  return <AuthContext.Provider value={value}>{isInitialized && children}</AuthContext.Provider>;
}
