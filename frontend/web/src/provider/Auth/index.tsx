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
    alert('[DEBUG] 로컬 상태 정리 완료');

    try {
      alert('[DEBUG] FCM 토큰 삭제 시도');
      await deleteFCMToken();
      alert('[DEBUG] FCM 토큰 삭제 성공');
    } catch (error) {
      console.error('FCM 토큰 삭제 실패:', error);
      alert('[DEBUG] FCM 토큰 삭제 실패: ' + (error as any)?.message);
    }

    try {
      alert('[DEBUG] 로그아웃 API 호출 시도');
      await logout();
      alert('[DEBUG] 로그아웃 API 성공');
    } catch (error) {
      console.error('로그아웃 API 실패:', error);
      alert('[DEBUG] 로그아웃 API 실패: ' + (error as any)?.message);
    }

    // 마지막에 리다이렉트
    alert('[DEBUG] 홈으로 리다이렉트 시도');
    window.location.href = '/';
  }, [logout, deleteFCMToken]);

  const refreshTokenRegularly = useCallback(async () => {
    alert('[DEBUG] refreshTokenRegularly 시작');
    try {
      alert('[DEBUG] refreshToken API 호출 시도');
      await refreshToken();
      alert('[DEBUG] refreshToken API 성공');
    } catch (error) {
      console.error('Token refresh failed:', error);
      alert('[DEBUG] refreshToken 실패! 에러: ' + (error as any)?.response?.status);

      // 즉시 상태 업데이트 (다른 API 호출 막기)
      alert('[DEBUG] 즉시 상태 업데이트 시작');
      setIsAuthenticated(false);
      localStorage.removeItem('isAuthenticated');
      localStorage.removeItem('nickname');
      alert('[DEBUG] 상태 업데이트 완료 - isAuthenticated = false');

      alert('[DEBUG] handleLogout 호출 예정');
      await handleLogout();
      alert('[DEBUG] handleLogout 완료 (여기는 도달 안 할 수도 있음 - 리다이렉트됨)');
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
      alert('[DEBUG] AuthProvider initialize 시작');
      const savedAuthStatus = localStorage.getItem('isAuthenticated') === 'true';
      alert('[DEBUG] savedAuthStatus: ' + savedAuthStatus);

      if (savedAuthStatus && !isReactNativeWebView) {
        alert('[DEBUG] 웹 환경 - refreshTokenRegularly 호출 예정');
        await refreshTokenRegularly(); // ✅ 완료될 때까지 대기
        alert('[DEBUG] refreshTokenRegularly 완료');
      } else if (savedAuthStatus && isReactNativeWebView) {
        alert('[DEBUG] 웹뷰 환경');
        // 웹뷰 환경: authToken이 있어도 토큰 검증 필요
        const hasAuthToken = localStorage.getItem('authToken');
        if (hasAuthToken) {
          alert('[DEBUG] authToken 존재 - 토큰 검증 시도');
          // authToken이 있어도 만료되었을 수 있으므로 검증
          await refreshTokenRegularly();
          alert('[DEBUG] 웹뷰 토큰 검증 완료');
        } else {
          alert('[DEBUG] authToken 없음 - 모바일에 토큰 갱신 요청');
          // authToken이 없으면 토큰 만료 가능성 → 갱신 요청
          window.ReactNativeWebView?.postMessage(
            JSON.stringify({
              type: 'REQUEST_REFRESH_TOKEN',
            }),
          );
        }
      } else {
        alert('[DEBUG] 로그인되지 않은 상태');
      }

      alert('[DEBUG] setIsInitialized(true) 실행');
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
