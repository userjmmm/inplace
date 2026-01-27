import { initializeConfig } from "@inplace-frontend-monorepo/shared/src/api/config/index";

// EXPO_PUBLIC_ENV 환경 변수로 환경 결정
// 설정되지 않은 경우 __DEV__ 플래그 사용
const environment = process.env.EXPO_PUBLIC_ENV === "development"
  ? "development"
  : process.env.EXPO_PUBLIC_ENV === "production"
  ? "production"
  : __DEV__ ? "development" : "production";

initializeConfig(environment);

import WebViewScreen from "./src/components/common/WebViewScreen";

export default function App() {
  return <WebViewScreen />;
}
