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

const ACCESS_TOKEN_REFRESH_INTERVAL = 10 * 60 * 1000;

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
      const isReactNativeWebView = typeof window !== 'undefined' && window.ReactNativeWebView != null;

      console.log('[AuthProvider] 초기화 시작');
      console.log('[AuthProvider] savedAuthStatus:', savedAuthStatus);
      console.log('[AuthProvider] isReactNativeWebView:', isReactNativeWebView);
      console.log('[AuthProvider] localStorage.authToken:', localStorage.getItem('authToken') ? '있음' : '없음');
      console.log('[AuthProvider] localStorage.nickname:', localStorage.getItem('nickname'));

      if (savedAuthStatus && !isReactNativeWebView) {
        // WebView가 아닐 때만 초기 refreshToken 호출
        console.log('[AuthProvider] refreshToken 호출 시도');
        try {
          await refreshTokenRegularly();
          console.log('[AuthProvider] refreshToken 성공');
        } catch (error) {
          console.error('[AuthProvider] refreshToken 실패, 로그아웃 처리:', error);
          handleLogout();
        }
      } else {
        console.log('[AuthProvider] refreshToken 건너뜀 (WebView 또는 미로그인)');
      }

      console.log('[AuthProvider] 초기화 완료, isAuthenticated:', savedAuthStatus);
      setIsInitialized(true);
    };

    initialize();
  }, [refreshTokenRegularly, handleLogout]);

  useEffect(() => {
    let intervalId: NodeJS.Timeout;

    if (isAuthenticated) {
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
