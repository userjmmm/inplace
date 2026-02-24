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
  };
};
