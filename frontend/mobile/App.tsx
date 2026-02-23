import { initializeConfig } from "@inplace-frontend-monorepo/shared/src/api/config/index";
import { useEffect } from "react";

const environment = process.env.EXPO_PUBLIC_ENV === "development"
  ? "development"
  : process.env.EXPO_PUBLIC_ENV === "production"
  ? "production"
  : __DEV__ ? "development" : "production";

initializeConfig(environment);

import WebViewScreen from "./src/components/common/WebViewScreen";

// Firebase 초기화 상태 체크 (앱 로드 시점)
try {
  // @ts-ignore - Firebase 네이티브 모듈 접근
  const { firebase } = require("@react-native-firebase/app");
  if (firebase && firebase.apps && firebase.apps.length > 0) {
    alert(
      `[DEBUG] App.tsx 로드 시점\n✅ Firebase 초기화됨\n앱 개수: ${firebase.apps.length}\n앱 이름: ${firebase.apps[0]?.name || "unknown"}`
    );
  } else {
    alert(
      "[DEBUG] App.tsx 로드 시점\n❌ Firebase 초기화 안됨"
    );
  }
} catch (error: any) {
  // @react-native-firebase/app이 설치되지 않은 경우 (Expo는 기본적으로 네이티브 Firebase를 사용하지 않음)
  console.log("[DEBUG] @react-native-firebase/app을 사용하지 않음:", error.message);
  alert(
    `[DEBUG] App.tsx 로드 시점\nℹ️ @react-native-firebase/app 미설치\n(Expo FCM 사용 중)\n에러: ${error.message}`
  );
}

export default function App() {
  useEffect(() => {
    // 앱 마운트 후 Firebase 초기화 상태 재확인
    setTimeout(() => {
      try {
        // @ts-ignore
        const { firebase } = require("@react-native-firebase/app");
        const currentStatus = firebase?.apps?.length > 0;
        alert(
          `[DEBUG] useEffect 실행 후 (1초 뒤)\n${currentStatus ? "✅ 초기화됨" : "❌ 초기화 안됨"}\n앱 개수: ${firebase?.apps?.length || 0}`
        );
      } catch (error: any) {
        alert(
          `[DEBUG] useEffect 실행 후\nExpo FCM 사용 중 (네이티브 Firebase 미설치)`
        );
      }
    }, 1000);
  }, []);

  return <WebViewScreen />;
}
