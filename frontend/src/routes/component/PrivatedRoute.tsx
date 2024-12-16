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

  const {
    data: userInfo,
    isLoading,
    error,
  } = useGetUserInfo({
    retry: false,
    enabled: !isAuthenticated,
    onSuccess: (data) => {
      if (data?.nickname && !isAuthenticated) {
        handleLoginSuccess(data.nickname);
      }
    },
  });

  useEffect(() => {
    if (isLoading) return;

    if (error && isAuthorizationError(error)) {
      console.log('[PrivateRoute] Handling auth error');
      if (isProtectedPath) {
        navigate('/', { replace: true });
        return;
      }
      setShouldShowModal(true);
      return;
    }

    if (isProtectedPath && !isAuthenticated && !userInfo?.nickname) {
      navigate('/', { replace: true });
      return;
    }

    if (!isAuthenticated && !isProtectedPath) {
      setShouldShowModal(true);
    }
  }, [error, isProtectedPath, isAuthenticated, isLoading, userInfo, navigate]);

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
