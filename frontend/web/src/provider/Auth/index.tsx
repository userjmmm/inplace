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

const ACCESS_TOKEN_REFRESH_INTERVAL = 10 * 30 * 1000; // 30초마다 갱신

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
    alert('[DEBUG] handleLogout 시작');

    if (window.ReactNativeWebView) {
      window.ReactNativeWebView.postMessage(
        JSON.stringify({
          type: 'LOGOUT',
        }),
      );
      alert('[DEBUG] 웹뷰 환경 - 모바일 앱에 로그아웃 요청 전달');
      return;
    }

    alert('[DEBUG] 웹 환경 - 로컬 상태 정리 시작');
    // API 호출 전에 먼저 로컬 상태 정리
    setIsAuthenticated(false);
    localStorage.removeItem('isAuthenticated');
    localStorage.removeItem('nickname');

    try {
      await deleteFCMToken();
    } catch (error) {
      console.error('FCM 토큰 삭제 실패:', error);
    }

    try {
      await logout();
    } catch (error) {
      console.error('로그아웃 API 실패:', error);
    }

    // 마지막에 리다이렉트
    window.location.href = '/';
  }, [logout, deleteFCMToken]);

  const refreshTokenRegularly = useCallback(async () => {
    try {
      await refreshToken();
    } catch (error) {
      console.error('Token refresh failed:', error);

      // 즉시 상태 업데이트 (다른 API 호출 막기)
      setIsAuthenticated(false);
      localStorage.removeItem('isAuthenticated');
      localStorage.removeItem('nickname');

      await handleLogout();
    }
  }, [refreshToken, handleLogout]);

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
        await refreshTokenRegularly(); // ✅ 완료될 때까지 대기
      } else if (savedAuthStatus && isReactNativeWebView) {
        const hasAuthToken = localStorage.getItem('authToken');
        if (!hasAuthToken) {
          // authToken이 없으면 native에 갱신 요청 (native가 토큰 관리)
          window.ReactNativeWebView?.postMessage(
            JSON.stringify({
              type: 'REQUEST_REFRESH_TOKEN',
            }),
          );
        }
        // authToken이 있으면 native가 토큰 갱신을 직접 관리하므로 쿠키 기반 refresh 호출하지 않음
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
