const { withAndroidManifest } = require("@expo/config-plugins");
require("dotenv").config();

module.exports = ({ config }) => {
  const KAKAO_APP_KEY = process.env.EXPO_PUBLIC_KAKAO_NATIVE_APP_KEY;

  // 1. AndroidManifest.xml을 직접 수정하는 플러그인 정의
  const withKakaoNativeAppKey = (expoConfig) => {
    return withAndroidManifest(expoConfig, async (config) => {
      const androidManifest = config.modResults;
      const mainApplication = androidManifest.manifest.application[0];

      // meta-data 배열이 없으면 생성
      if (!mainApplication["meta-data"]) {
        mainApplication["meta-data"] = [];
      }

      // 이미 키가 있는지 확인 (중복 방지)
      const existingKey = mainApplication["meta-data"].find(
        (item) => item.$["android:name"] === "com.kakao.sdk.AppKey"
      );

      if (existingKey) {
        existingKey.$["android:value"] = KAKAO_APP_KEY;
      } else {
        // 키가 없으면 새로 추가
        mainApplication["meta-data"].push({
          $: {
            "android:name": "com.kakao.sdk.AppKey",
            "android:value": KAKAO_APP_KEY,
          },
        });
      }

      return config;
    });
  };

  // 2. 기본 설정 객체 생성
  const finalConfig = {
    ...config,
    name: "mobile",
    slug: "mobile",
    version: "1.0.0",
    orientation: "portrait",
    icon: "./assets/icon.png",
    userInterfaceStyle: "light",
    newArchEnabled: true,
    splash: {
      image: "./assets/splash-icon.png",
      resizeMode: "contain",
      backgroundColor: "#292929",
    },
    ios: {
      supportsTablet: true,
      bundleIdentifier: "my.inplace.mobile",
      infoPlist: {
        LSApplicationQueriesSchemes: [
          "kakaokompassauth",
          "kakaolink",
          `kakao${process.env.EXPO_PUBLIC_KAKAO_NATIVE_APP_KEY}`,
        ],
        CFBundleURLTypes: [
          {
            CFBundleTypeRole: "Editor",
            CFBundleURLSchemes: [
              `kakao${process.env.EXPO_PUBLIC_KAKAO_NATIVE_APP_KEY}`,
            ],
          },
        ],
      },
    },
    android: {
      package: "my.inplace.mobile",
      adaptiveIcon: {
        foregroundImage: "./assets/adaptive-icon.png",
        backgroundColor: "#292929",
      },
      intentFilters: [
        {
          action: "VIEW",
          autoVerify: true,
          data: [
            {
              scheme: `kakao${process.env.EXPO_PUBLIC_KAKAO_NATIVE_APP_KEY}`,
              host: "oauth",
            },
          ],
          category: ["DEFAULT", "BROWSABLE"],
        },
      ],
    },
    web: {
      favicon: "./assets/favicon.png",
    },
    plugins: [
      [
        "expo-secure-store",
        {
          configureAndroidBackup: true,
          faceIDPermission:
            "Allow $(inplace) to access your Face ID biometric data.",
        },
      ],
      [
        "expo-build-properties",
        {
          android: {
            extraMavenRepos: [
              "https://devrepo.kakao.com/nexus/content/groups/public/",
            ],
          },
        },
      ],
      [
        "@react-native-kakao/core",
        {
          nativeAppKey: process.env.EXPO_PUBLIC_KAKAO_NATIVE_APP_KEY,
          android: { authCodeHandlerActivity: true },
          ios: { handleKakaoOpenUrl: true },
        },
      ],
    ],
    extra: {
      eas: {
        projectId: "1f938aec-ae98-4f0c-ab7a-c4c37114e2a7",
      },
    },
  };

  // 3. 위에서 정의한 플러그인을 적용해서 반환
  return withKakaoNativeAppKey(finalConfig);
};