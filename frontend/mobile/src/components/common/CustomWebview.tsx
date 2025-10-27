import React, { forwardRef, useEffect, useRef, useState } from "react";
import { WebView, WebViewMessageEvent } from "react-native-webview";
import { BackHandler, Platform, SafeAreaView, StyleSheet } from "react-native";

export interface CustomWebViewProps {
  url: string;
  onMessage?: (event: WebViewMessageEvent) => void;
}

const CustomWebView = forwardRef<WebView, CustomWebViewProps>(
  ({ url, onMessage }, ref) => {
    const webViewRef = useRef<WebView>(null);
    const [backPressedOnce, setBackPressedOnce] = useState(false);

    // ref를 부모에게 전달하면서 내부에서도 사용
    useEffect(() => {
      if (ref) {
        if (typeof ref === 'function') {
          ref(webViewRef.current);
        } else {
          ref.current = webViewRef.current;
        }
      }
    }, [ref]);

    useEffect(() => {
      if (Platform.OS !== 'android') return;

      const backAction = () => {
        // 웹에서 뒤로가기 처리하도록 메시지 전송
        if (webViewRef.current) {
          webViewRef.current.postMessage(JSON.stringify({ type: 'BACK_PRESSED' }));
          return true;
        }

        // WebView가 없으면 앱 종료 로직
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
    }, [backPressedOnce]);

    return (
      <SafeAreaView style={styles.container}>
        <WebView
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
          ref={webViewRef}
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