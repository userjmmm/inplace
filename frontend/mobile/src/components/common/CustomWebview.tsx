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
    const [canGoBack, setCanGoBack] = useState(false);
    const webViewRef = useRef<WebView | null>(null);

    useEffect(() => {
      const backAction = () => {
        // 웹뷰에서 뒤로갈 수 있으면 웹뷰 히스토리에서 뒤로가기
        if (canGoBack && webViewRef.current) {
          webViewRef.current.goBack();
          return true;
        }

        // 웹뷰에 뒤로가기 메시지 전송 (웹앱 내부 라우터 처리용)
        if (webViewRef.current) {
          const message = JSON.stringify({
            type: 'BACK_BUTTON_PRESSED',
            data: {}
          });
          webViewRef.current.postMessage(message);
          return true;
        }

        // 더 이상 뒤로갈 곳이 없으면 앱 종료 확인
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
      );
      
      return () => backHandler.remove();
    }, [backPressedOnce, canGoBack]);

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
          onNavigationStateChange={(navState) => {
            // WebView의 히스토리 상태 추적
            setCanGoBack(navState.canGoBack);
          }}
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
              // 웹앱에서 히스토리 상태를 알려줄 수도 있음
              if (data.type === 'HISTORY_STATE') {
                setCanGoBack(data.canGoBack);
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