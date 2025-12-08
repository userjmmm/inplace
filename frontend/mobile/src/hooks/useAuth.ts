import { login } from "@react-native-kakao/user";
import { getKeyHashAndroid } from "@react-native-kakao/core";
import { getAccessToken } from "../api/getAccessToken";
import * as SecureStore from "expo-secure-store";
import WebView from "react-native-webview";
import * as Application from "expo-application";

export const useAuth = (webViewRef: React.RefObject<WebView | null>) => {
  const handleKakaoLogin = async () => {
    try {
      // 1. 진단 데이터 수집
      const currentHash = await getKeyHashAndroid();
      const currentPackageName = Application.applicationId;
      const configuredAppKey = process.env.EXPO_PUBLIC_KAKAO_NATIVE_APP_KEY;

      const debugMessage = `[진단 정보]
1. 패키지명: ${currentPackageName}
2. 앱 키: ${configuredAppKey}
3. 해시 키: ${currentHash}`;

      if (webViewRef.current) {
        webViewRef.current.injectJavaScript(`alert(\`${debugMessage}\`); true;`);
      }

      if (webViewRef.current) {
        webViewRef.current.injectJavaScript(`alert('1. 카카오 로그인 시작'); true;`);
      }

      const kakaoToken = await login();

      if (webViewRef.current) {
        webViewRef.current.injectJavaScript(`alert('2. 카카오 토큰 받기 성공'); true;`);
      }

      const tokens = await getAccessToken(kakaoToken.accessToken);

      if (tokens) {
        const { accessToken, refreshToken } = tokens;

        if (webViewRef.current) {
          webViewRef.current.injectJavaScript(`alert('3. 백엔드 토큰 받기 성공'); true;`);
        }

        await Promise.all([
          SecureStore.setItemAsync("accessToken", accessToken),
          SecureStore.setItemAsync("refreshToken", refreshToken),
        ]);

        if (webViewRef.current) {
          const script = `
          window.localStorage.setItem('authToken', '${accessToken}');
          window.setAuthToken('${accessToken}');
          alert('4. 토큰 저장 완료, /auth로 이동');
          window.location.href = '/auth';
          true;
        `;
          webViewRef.current.injectJavaScript(script);
        }
      } else {
        if (webViewRef.current) {
          webViewRef.current.injectJavaScript(`alert('에러: 백엔드에서 토큰 못받음'); true;`);
        }
      }
    } catch (error) {
      if (webViewRef.current) {
        const errorMsg = String(error).replace(/'/g, "\\'");
        webViewRef.current.injectJavaScript(`alert('카카오 로그인 실패: ${errorMsg}'); true;`);
      }
    }
  };
  return { handleKakaoLogin };
};
