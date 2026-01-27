import { login, me } from "@react-native-kakao/user";
import { getAccessToken } from "../api/getAccessToken";
import * as SecureStore from "expo-secure-store";
import WebView from "react-native-webview";

export const useAuth = (webViewRef: React.RefObject<WebView | null>) => {
  const handleKakaoLogin = async () => {
    try {
      await login();

      const userProfile = await me();

      const userInfo = {
        nickname: userProfile.nickname ?? "Guest",
        username: userProfile.email ?? "",
        profileImageUrl: userProfile.thumbnailImageUrl ?? "",
      };

      const tokens = await getAccessToken(userInfo);

      alert(`useAuth - tokens 받음: ${tokens ? 'O' : 'X'}`);

      if (tokens) {
        const { accessToken, refreshToken } = tokens;

        alert(`토큰 저장 시작...`);

        await Promise.all([
          SecureStore.setItemAsync("accessToken", accessToken),
          SecureStore.setItemAsync("refreshToken", refreshToken),
        ]);

        alert(`SecureStore 저장 완료!`);

        if (webViewRef.current) {
          alert(`웹뷰에 토큰 주입 시작...`);
          const script = `
          window.localStorage.setItem('authToken', '${accessToken}');
          window.setAuthToken('${accessToken}');
          true;
        `;
          webViewRef.current.injectJavaScript(script);

          alert("로그인 성공 및 웹뷰에 토큰 주입 완료!");
          console.log("로그인 성공 및 웹뷰에 토큰 주입 완료!");
        } else {
          alert("webViewRef.current가 없습니다!");
        }
      } else {
        alert("tokens가 null입니다!");
      }
    } catch (error) {
      alert(`카카오 로그인 실패: ${error}`);
      console.error("카카오 로그인 실패:", error);
    }
  };
  return { handleKakaoLogin };
};
