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
            (function() {
              alert('[1] localStorage 저장 시작');

              window.localStorage.setItem('authToken', '${accessToken}');
              window.localStorage.setItem('isAuthenticated', 'true');
              window.localStorage.setItem('nickname', '${userInfo.nickname}');

              alert('[2] 저장 완료 - authToken: ' + (window.localStorage.getItem('authToken') ? 'O' : 'X'));
              alert('[3] 저장 완료 - isAuthenticated: ' + window.localStorage.getItem('isAuthenticated'));
              alert('[4] 저장 완료 - nickname: ' + window.localStorage.getItem('nickname'));

              if (window.setAuthToken) {
                window.setAuthToken('${accessToken}');
                alert('[5] setAuthToken 호출 완료');
              } else {
                alert('[5] setAuthToken이 없습니다!');
              }

              const redirectPath = window.localStorage.getItem('redirectPath');
              alert('[6] redirectPath: ' + (redirectPath || '없음'));

              if (redirectPath) {
                window.localStorage.removeItem('redirectPath');
                alert('[7] redirectPath로 이동: ' + redirectPath);
                window.location.href = redirectPath;
              } else {
                alert('[7] 메인으로 이동');
                window.location.href = '/';
              }
            })();
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
