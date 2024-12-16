import { ReactElement, useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import useAuth from '@/hooks/useAuth';
import LoginModal from '@/components/common/modals/LoginModal';
import { useGetUserInfo, isAuthorizationError } from '@/api/hooks/useGetUserInfo';

type PrivateRouteProps = {
  children: ReactElement;
};

export default function PrivateRoute({ children }: PrivateRouteProps) {
  const [shouldShowModal, setShouldShowModal] = useState(false);
  const { isAuthenticated, handleLoginSuccess } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const directRedirectPaths = ['/choice', '/auth'];
  const isProtectedPath = directRedirectPaths.includes(location.pathname);

  console.log('[PrivateRoute] Current state:', {
    path: location.pathname,
    isAuthenticated,
  });

  const { data: userInfo, isLoading } = useGetUserInfo({
    retry: false,
    enabled: true,
    onSuccess: (data) => {
      if (data?.nickname && !isAuthenticated) {
        handleLoginSuccess(data.nickname);
      }
    },
    onError: (error: unknown) => {
      console.log('Error occurred:', error);

      if (isAuthorizationError(error)) {
        console.log('[PrivateRoute] Unauthorized access:', {
          message: error.response.data.message,
          status: error.response.status,
        });

        if (isProtectedPath) {
          navigate('/', { replace: true });
        } else {
          setShouldShowModal(true);
        }
        return;
      }

      console.error('사용자 정보 요청 실패:', error);
      if (isProtectedPath) {
        navigate('/', { replace: true });
      } else {
        setShouldShowModal(true);
      }
    },
  });

  useEffect(() => {
    if (isProtectedPath && !isAuthenticated && !isLoading && !userInfo?.nickname) {
      navigate('/', { replace: true });
    }
  }, [isProtectedPath, isAuthenticated, userInfo, isLoading, navigate]);

  const handleCloseModal = () => {
    if (window.history.length > 2) {
      navigate(-1);
    } else {
      navigate('/');
    }
  };

  const handleModalSuccess = async () => {
    setShouldShowModal(false);
    if (userInfo?.nickname) {
      await handleLoginSuccess(userInfo.nickname);
    }
  };

  useEffect(() => {
    if (userInfo?.nickname && !isAuthenticated) {
      handleLoginSuccess(userInfo.nickname);
    }
  }, [userInfo, isAuthenticated, handleLoginSuccess]);

  if (isLoading) return null;

  if (shouldShowModal && !isAuthenticated) {
    return (
      <LoginModal
        currentPath={location.pathname}
        immediateOpen
        onClose={handleCloseModal}
        onLoginSuccess={handleModalSuccess}
      />
    );
  }
  if (userInfo?.nickname || isAuthenticated) {
    return children;
  }

  return null;
}
