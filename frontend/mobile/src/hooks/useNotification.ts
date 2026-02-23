import { Platform } from "react-native";
import WebView from "react-native-webview";
import * as Notifications from "expo-notifications";

export const useNotification = (webViewRef: React.RefObject<WebView | null>) => {
  const handleNotificationPermission = async (): Promise<void> => {
    try {
      const { status: existingStatus } =
        await Notifications.getPermissionsAsync();
      
      const finalStatus = existingStatus !== "granted" // 권한이 없으면 요청(없거나 거부된 상태)
        ? (await Notifications.requestPermissionsAsync()).status
        : existingStatus;

      if (finalStatus !== "granted") {
        console.log("알림 권한이 거부되었습니다");
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
      await getExpoPushToken();
    } catch (error) {
      console.error("알림 권한 요청 실패:", error);
    }
  };

  const getExpoPushToken = async () => {
    try {
      alert("[DEBUG] getExpoPushToken 함수 시작");

      // Firebase 초기화 상태 체크
      try {
        // @ts-ignore
        const { firebase } = require("@react-native-firebase/app");
        const isInitialized = firebase?.apps?.length > 0;
        alert(
          `[DEBUG] getExpoPushToken 시작 시점\nFirebase 초기화: ${isInitialized ? "✅ 됨" : "❌ 안됨"}\n앱 개수: ${firebase?.apps?.length || 0}`
        );
      } catch (e: any) {
        alert(
          `[DEBUG] getExpoPushToken 시작 시점\nFirebase 네이티브 모듈 없음 (Expo FCM 사용)`
        );
      }

      // 권한 확인
      let { status } = await Notifications.getPermissionsAsync();
      console.log(`[DEBUG] 초기 알림 권한 상태: ${status}`);
      alert(`[DEBUG] 초기 알림 권한 상태: ${status}`);

      // 권한이 없으면 요청
      if (status !== "granted") {
        console.log("알림 권한 요청 중...");
        alert("알림 권한 요청 중...");
        const result = await Notifications.requestPermissionsAsync();
        status = result.status;
        console.log(`[DEBUG] 권한 요청 후 상태: ${status}`);
        alert(`[DEBUG] 권한 요청 후 상태: ${status}`);
      }

      if (status !== "granted") {
        console.log("알림 권한이 거부되어 Expo Token을 가져올 수 없습니다");
        alert("알림 권한이 거부되어 Expo Token을 가져올 수 없습니다");
        return null;
      }

      alert("[DEBUG] getExpoPushTokenAsync 호출 시작 (app.json의 projectId 자동 사용)");

      // Firebase 초기화 상태 재확인 (토큰 요청 직전)
      try {
        // @ts-ignore
        const { firebase } = require("@react-native-firebase/app");
        alert(
          `[DEBUG] getExpoPushTokenAsync 호출 직전\nFirebase 앱 개수: ${firebase?.apps?.length || 0}`
        );
      } catch (e) {
        // Expo FCM 사용 중
      }

      const tokenData = await Notifications.getExpoPushTokenAsync();
      const expoPushToken = tokenData.data;

      console.log(`[DEBUG] Expo Token 발급 성공: ${expoPushToken}`);
      alert(`[DEBUG] Expo Token 발급 성공: ${expoPushToken}`);

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
    } catch (tokenError: any) {
      console.error("Expo Token 가져오기 실패:", tokenError);

      // 에러 정보 상세 출력
      const errorMessage = tokenError?.message || String(tokenError);
      const errorName = tokenError?.name || "알 수 없는 에러";
      const isFirebaseError = errorMessage.includes("Firebase") || errorMessage.includes("firebase");
      const isInitializeError = errorMessage.includes("initialize") || errorMessage.includes("initialized");

      alert(
        `[ERROR] Expo Token 가져오기 실패\n\n` +
        `에러 타입: ${errorName}\n` +
        `메시지: ${errorMessage}\n\n` +
        `Firebase 관련: ${isFirebaseError ? "✅ 예" : "❌ 아니오"}\n` +
        `초기화 에러: ${isInitializeError ? "✅ 예" : "❌ 아니오"}`
      );

      console.error("에러 스택:", tokenError?.stack);

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
  };
};
