import { useEffect, useRef } from "react";
import { SafeAreaView, StyleSheet } from "react-native";
import WebView from "react-native-webview";
import { WEB_VIEW_URL } from "./src/utils/constants/webURL";
import CustomWebView from "./src/components/common/CustomWebview";
import { useLocation } from "./src/hooks/useLocation";
import LocationPermissionModal from "./src/components/location/LocationPermissionModal";

export default function WebViewScreen() {
  const webViewRef = useRef<WebView | null>(null);
  const { modalVisible, modalContent, showLocationModal, hideModal } =
    useLocation(webViewRef);

  const handleMessage = (event: any) => {
    const message = JSON.parse(event.nativeEvent.data);
    switch (message.type) {
      case "GPS_PERMISSIONS":
        showLocationModal();
        break;
      case "APP_EXIT":
        // 이미 CustomWebView에서 처리되므로 여기서는 아무것도 하지 않음
        break;
      // case "AUTH_TOKEN":
      //   saveToken(message.payload);
      //   break;
      // case "LOGOUT":
      //   removeToken();
      //   break;
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <CustomWebView
        ref={webViewRef}
        url={WEB_VIEW_URL}
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
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({ container: { flex: 1 } });
