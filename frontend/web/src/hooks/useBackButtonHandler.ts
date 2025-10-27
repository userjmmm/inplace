import { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

interface PostMessageData {
  type: string;
  data?: unknown;
}

interface MessageEventWithData extends Event {
  data: string;
}

const useBackButtonHandler = () => {
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const handleMessage = (messageData: PostMessageData) => {
      if (messageData.type === 'BACK_BUTTON_PRESSED') {
        // 현재 위치가 홈페이지가 아니면 뒤로가기
        if (location.pathname !== '/') {
          navigate(-1);
        } else if (window.ReactNativeWebView) {
          // 홈페이지에서는 앱 종료 메시지를 보냄
          window.ReactNativeWebView.postMessage(
            JSON.stringify({
              type: 'APP_EXIT',
            }),
          );
        }
      }
    };

    const handleWindowMessage = (event: MessageEvent) => {
      try {
        const messageData: PostMessageData = JSON.parse(event.data);
        handleMessage(messageData);
      } catch (error) {
        // JSON 파싱 에러 무시 (다른 메시지일 수 있음)
      }
    };

    const handleDocumentMessage = (event: Event) => {
      try {
        const messageData: PostMessageData = JSON.parse((event as MessageEventWithData).data);
        handleMessage(messageData);
      } catch (error) {
        // JSON 파싱 에러 무시 (다른 메시지일 수 있음)
      }
    };

    // iOS와 Android 모두 지원
    window.addEventListener('message', handleWindowMessage);
    document.addEventListener('message', handleDocumentMessage);

    return () => {
      // 컴포넌트 언마운트 시 이벤트 리스너 제거
      window.removeEventListener('message', handleWindowMessage);
      document.removeEventListener('message', handleDocumentMessage);
    };
  }, [navigate, location.pathname]);
};

export default useBackButtonHandler;
