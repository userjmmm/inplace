import { initializeConfig } from "@inplace-frontend-monorepo/shared/src/api/config/index";
initializeConfig(__DEV__ ? "development" : "production");

import WebViewScreen from "./src/components/common/WebViewScreen";
import { useEffect } from "react";
import { Alert, Platform, NativeModules } from "react-native";

export default function App() {
  useEffect(() => {
    if (Platform.OS === "android") {
      // 안드로이드 네이티브 모듈로 키 해시 확인
      const getKeyHash = async () => {
        try {
          // @react-native-kakao/core에서 제공하는 키 해시 확인 방법
          const RNKakaoLogins = NativeModules.RNKakaoLogins;
          if (RNKakaoLogins && RNKakaoLogins.getKeyHash) {
            const keyHash = await RNKakaoLogins.getKeyHash();
            Alert.alert("카카오 키 해시", keyHash);
          } else {
            Alert.alert("키 해시 확인", "네이티브 모듈을 찾을 수 없습니다.");
          }
        } catch (error) {
          Alert.alert("키 해시 오류", String(error));
        }
      };
      getKeyHash();
    }
  }, []);

  return <WebViewScreen />;
}
