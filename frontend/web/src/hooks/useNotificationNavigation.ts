import { useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { onMessage } from 'firebase/messaging';
import { messaging } from '@/libs/FCM';
import { getCommentCurrentPage } from '@/api/hooks/useGetCommentCurrentPage';

interface MobileNotificationClickEvent extends CustomEvent {
  detail: { postId: string; commentId: string };
}

export default function useNotificationNavigation() {
  const navigate = useNavigate();

  const handleNavigate = useCallback(
    async (postId: string, commentId: string) => {
      try {
        const commentPage = await getCommentCurrentPage(postId, Number(commentId));
        navigate(`/post/${postId}?commentPage=${commentPage}&commentId=${commentId}`);
      } catch (error) {
        console.error('알림 이동 처리 실패:', error);
      }
    },
    [navigate],
  );

  // 앱(웹뷰): RN에서 injectJavaScript로 dispatch되는 커스텀 이벤트
  useEffect(() => {
    const handler = (event: Event) => {
      const { postId, commentId } = (event as MobileNotificationClickEvent).detail || {};
      if (postId && commentId) {
        handleNavigate(postId, commentId);
      }
    };
    window.addEventListener('mobileNotificationClick', handler);
    return () => window.removeEventListener('mobileNotificationClick', handler);
  }, [handleNavigate]);

  // 웹 포그라운드: Firebase onMessage
  useEffect(() => {
    if (!messaging) return () => {};
    const unsubscribe = onMessage(messaging, (payload) => {
      const { postId, commentId } = (payload.data || {}) as { postId?: string; commentId?: string };
      if (postId && commentId) {
        handleNavigate(postId, commentId);
      }
    });
    return unsubscribe;
  }, [handleNavigate]);

  // 웹 백그라운드: SW notificationclick이 열어준 URL params 처리
  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const postId = params.get('notifPostId');
    const commentId = params.get('notifCommentId');
    if (postId && commentId) {
      params.delete('notifPostId');
      params.delete('notifCommentId');
      const rest = params.toString();
      window.history.replaceState(null, '', `${window.location.pathname}${rest ? `?${rest}` : ''}`);
      handleNavigate(postId, commentId);
    }
  }, [handleNavigate]);
}
