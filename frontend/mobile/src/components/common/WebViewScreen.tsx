import { useEffect, useRef, useState } from "react";
import { ActivityIndicator, SafeAreaView, StyleSheet } from "react-native";
import WebView from "react-native-webview";
import { useLocation } from "../../hooks/useLocation";
import CustomWebView from "./CustomWebview";
import LocationPermissionModal from "../location/LocationPermissionModal";
import { useWebViewMessageHandler } from "../../hooks/useWebViewMessageHandler";
import { useAuth } from "../../hooks/useAuth";
import { getConfig } from "@inplace-frontend-monorepo/shared/src/api/config";
import { useRefreshToken } from "../../hooks/useRefreshToken";
import { initializeKakaoSDK } from "@react-native-kakao/core";
import { useLogout } from "../../hooks/useLogout";
import { useNotification } from "../../hooks/useNotification";

export default function WebViewScreen() {
  const config = getConfig();
  const webViewRef = useRef<WebView | null>(null);
  const [isWebViewReady, setWebViewReady] = useState(false);

  const { modalVisible, modalContent, showLocationModal, hideModal } =
    useLocation(webViewRef);
  const { handleKakaoLogin } = useAuth(webViewRef);
  const { handleRefreshToken } = useRefreshToken(webViewRef);
  const { handleLogout } = useLogout(webViewRef);
  const { handleNotificationPermission } = useNotification(webViewRef);
  const { handleMessage } = useWebViewMessageHandler({
    onGpsPermissionRequest: showLocationModal,
    onLoginWithKakao: handleKakaoLogin,
    onRefreshToken: handleRefreshToken,
    onLogout: handleLogout,
    onNotificationPermission: handleNotificationPermission,
  });
  useEffect(() => {
    const init = async () => {
      initializeKakaoSDK(process.env.EXPO_PUBLIC_KAKAO_NATIVE_APP_KEY);

      // 키 해시 확인 (임시 디버깅용)
      try {
        const { NativeModules } = require("react-native");
        const { RNCKakaoCore } = NativeModules;
        if (RNCKakaoCore && RNCKakaoCore.getKeyHash) {
          const keyHash = await RNCKakaoCore.getKeyHash();
          console.log("Kakao KeyHash:", keyHash);
          if (webViewRef.current) {
            webViewRef.current.injectJavaScript(`
              alert('카카오 키 해시:\\n${keyHash}\\n\\n이 값을 카카오 개발자 콘솔에 등록하세요.');
              true;
            `);
          }
        }
      } catch (e) {
        console.error("키 해시 가져오기 실패:", e);
      }

      setWebViewReady(true);
    };

    init();
  }, []);

  return (
    <SafeAreaView style={styles.container}>
      {isWebViewReady ? (
        <>
          <CustomWebView
            ref={webViewRef}
            url="https://ecalpni-dev.inplace.my/"
            onMessage={handleMessage}
          />
          {modalContent && (
            <LocationPermissionModal
              visible={modalVisible}
              title={modalContent.title}
              message={modalContent.message}
              onConfirm={() => {
                modalContent.onConfirm();
                hideModal();
              }}
              onClose={hideModal}
            />
          )}
        </>
      ) : (
        <ActivityIndicator style={styles.loader} size="large" color="#0000ff" />
      )}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  loader: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
});
