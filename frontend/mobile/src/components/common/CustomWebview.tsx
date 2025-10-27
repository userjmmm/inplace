import React, { forwardRef, useEffect, useState, useRef } from "react";
import { WebView, WebViewMessageEvent } from "react-native-webview";
import { BackHandler, Platform, SafeAreaView, StyleSheet } from "react-native";

export interface CustomWebViewProps {
  url: string;
  onMessage?: (event: WebViewMessageEvent) => void;
}

const CustomWebView = forwardRef<WebView, CustomWebViewProps>(
  ({ url, onMessage }, ref) => {
    const [backPressedOnce, setBackPressedOnce] = useState(false);
    const webViewRef = useRef<WebView | null>(null);

    useEffect(() => {
      const backAction = () => {
        // 웹뷰에 뒤로가기 메시지 전송
        if (webViewRef.current) {
          const message = JSON.stringify({
            type: 'BACK_BUTTON_PRESSED',
            data: {}
          });
          webViewRef.current.postMessage(message);
          return true; // 기본 동작 막음
        }

        // 웹뷰가 없으면 기존 로직 실행
        if (backPressedOnce) {
          BackHandler.exitApp();
          return true;
        }
        setBackPressedOnce(true);
        setTimeout(() => {
          setBackPressedOnce(false);
        }, 2000);

        return true;
      };
      
      const backHandler = BackHandler.addEventListener(
        "hardwareBackPress",
        backAction
      )
      return () => backHandler.remove();
    }, [backPressedOnce]);

    return (
      <SafeAreaView style={styles.container}>
        <WebView
          ref={(webView) => {
            webViewRef.current = webView;
            if (typeof ref === 'function') {
              ref(webView);
            } else if (ref) {
              ref.current = webView;
            }
          }}
          source={{ uri: url }}
          onMessage={(event) => {
            try {
              const data = JSON.parse(event.nativeEvent.data);
              if (data.type === 'APP_EXIT') {
                if (backPressedOnce) {
                  BackHandler.exitApp();
                } else {
                  setBackPressedOnce(true);
                  setTimeout(() => setBackPressedOnce(false), 2000);
                }
              }
            } catch (e) {
              // JSON 파싱 에러 무시
            }
            onMessage?.(event);
          }}
          style={styles.webview}
          javaScriptEnabled={true}
          domStorageEnabled={true}
        />
      </SafeAreaView>
    );
  }
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: Platform.OS === "android" ? 25 : 0,
  },
  webview: {
    flex: 1,
  },
});

export default CustomWebView;