import { useCallback, useEffect, useRef } from "react";
import { Platform } from "react-native";
import WebView from "react-native-webview";
import * as Notifications from "expo-notifications";

export const useNotification = (webViewRef: React.RefObject<WebView | null>) => {
  const pendingNavigation = useRef<{ postId: string; commentId: string } | null>(null);

  const injectNavigationScript = useCallback(
    (postId: string, commentId: string) => {
      if (webViewRef.current) {
        const script = `
          window.dispatchEvent(new CustomEvent('mobileNotificationClick', {
            detail: { postId: '${postId}', commentId: '${commentId}' }
          }));
          true;
        `;
        webViewRef.current.injectJavaScript(script);
        return true;
      }
      return false;
    },
    [webViewRef],
  );

  const handlePendingNavigation = useCallback(() => {
    if (pendingNavigation.current) {
      const { postId, commentId } = pendingNavigation.current;
      injectNavigationScript(postId, commentId);
      pendingNavigation.current = null;
    }
  }, [injectNavigationScript]);

  // 앱 활성/백그라운드 상태에서 알림 클릭
  useEffect(() => {
    const subscription = Notifications.addNotificationResponseReceivedListener((response) => {
      const data = response.notification.request.content.data || {};
      const postId = String(data.postId ?? "");
      const commentId = String(data.commentId ?? "");
      if (postId && commentId) {
        const injected = injectNavigationScript(postId, commentId);
        if (!injected) {
          pendingNavigation.current = { postId, commentId };
        }
      }
    });
    return () => subscription.remove();
  }, [injectNavigationScript]);

  // 앱 콜드 스타트: 알림 클릭으로 앱이 시작된 경우
  useEffect(() => {
    Notifications.getLastNotificationResponseAsync().then((response) => {
      if (!response) return;
      const data = response.notification.request.content.data || {};
      const postId = String(data.postId ?? "");
      const commentId = String(data.commentId ?? "");
      if (postId && commentId) {
        pendingNavigation.current = { postId, commentId };
      }
    });
  }, []);

  const handleNotificationPermission = async (): Promise<void> => {
    try {
      const { status: existingStatus } =
        await Notifications.getPermissionsAsync();

      const finalStatus = existingStatus !== "granted"
        ? (await Notifications.requestPermissionsAsync()).status
        : existingStatus;

      if (finalStatus !== "granted") {
        console.log("알림 권한이 거부되었습니다");
        alert("[ERROR] 알림 권한이 거부되었습니다");
        return;
      }

      // 안드로이드에서 채널 설정
      if (Platform.OS === "android") {
        await Notifications.setNotificationChannelAsync("default", {
          name: "default",
          importance: Notifications.AndroidImportance.MAX,
          vibrationPattern: [0, 250, 250, 250],
          lightColor: "#FF231F7C",
        });
      }

      // 권한이 승인된 상태에서만 토큰 요청
      await getExpoPushToken();
    } catch (error) {
      console.error("알림 권한 요청 실패:", error);
      alert(`[ERROR] 알림 권한 요청 실패: ${error}`);
    }
  };

  const getExpoPushToken = async () => {
    try {
      const tokenData = await Notifications.getExpoPushTokenAsync();
      const expoPushToken = tokenData.data;
      if (webViewRef.current) {
        const script = `
          window.dispatchEvent(new CustomEvent('mobileNotificationPermission', {
            detail: { token: '${expoPushToken}', granted: true }
          }));
          true;
        `;
        webViewRef.current.injectJavaScript(script);
      }

      return expoPushToken;
    } catch (tokenError) {
      console.error("Expo Token 가져오기 실패:", tokenError);
      alert(`[ERROR] Expo Token 가져오기 실패: ${tokenError}`);
      // 토큰 가져오기는 실패해도 승인된 권한은 전달해줘야 됨
      if (webViewRef.current) {
        const script = `
          window.dispatchEvent(new CustomEvent('notificationPermission', {
            detail: { granted: true }
          }));
          true;
        `;
        webViewRef.current.injectJavaScript(script);
      }
      return null;
    }
  };

  return {
    handleNotificationPermission,
    getExpoPushToken,
    handlePendingNavigation,
  };
};
