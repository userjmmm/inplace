import { Route, Routes, useNavigate, useLocation } from 'react-router-dom';
import { lazy, useEffect, useRef } from 'react';
import AuthProvider from '@/provider/Auth';
import MainLayout from '@/components/common/layouts/MainLayout';
import PrivatedRoute from '@/routes/component/PrivatedRoute';
import AuthPage from '@/pages/Auth';

import GlobalStyle from './global';
import MainPage from './pages/Main';
import ThemeProvider from './provider/Themes';
import DetailPage from './pages/Detail';
import InfluencerInfoPage from '@/pages/InfluencerInfo';
import InfluencerPage from '@/pages/Influencer';
import MapPage from './pages/Map';
import ABTestProvider from './provider/ABTest';
import PostingPage from './pages/Posting';

const MyPage = lazy(() => import('@/pages/My'));
const ChoicePage = lazy(() => import('@/pages/Choice'));
const SearchPage = lazy(() => import('@/pages/Search'));
// const ReviewPage = lazy(() => import('@/pages/Review'));
const NotFoundPage = lazy(() => import('@/pages/NotFound'));
const PostPage = lazy(() => import('@/pages/Post'));
const PostDetailPage = lazy(() => import('@/pages/PostDetail'));

function AppContent() {
  const navigate = useNavigate();
  const location = useLocation();
  const navigationHistory = useRef<string[]>([location.pathname]);
  const isInitialized = useRef(false);

  useEffect(() => {
    if (!isInitialized.current) {
      isInitialized.current = true;
      return;
    }

    const currentPath = location.pathname;
    const lastPath = navigationHistory.current[navigationHistory.current.length - 1];

    if (currentPath !== lastPath) {
      navigationHistory.current.push(currentPath);
    }
  }, [location.pathname]);

  useEffect(() => {
    const handleMessage = (event: MessageEvent) => {
      if (event.data && event.data.type === 'BACK_PRESSED') {
        if (navigationHistory.current.length > 1) {
          navigationHistory.current.pop();
          navigate(-1);
        } else {
          window.ReactNativeWebView?.postMessage(JSON.stringify({ type: 'APP_EXIT' }));
        }
      }
    };

    window.addEventListener('message', handleMessage);
    return () => window.removeEventListener('message', handleMessage);
  }, [navigate]);

  return (
    <Routes>
      <Route path="/" element={<MainLayout />}>
        <Route index path="/" element={<MainPage />} />
        <Route path="/influencer" element={<InfluencerPage />} />
        <Route path="/influencer/:id" element={<InfluencerInfoPage />} />
        <Route path="/map" element={<MapPage />} />
        <Route path="/detail/:id" element={<DetailPage />} />
        <Route path="/search" element={<SearchPage />} />
        <Route path="/post" element={<PostPage />} />
        <Route
          path="/posting"
          element={
            <PrivatedRoute>
              <PostingPage />
            </PrivatedRoute>
          }
        />
        <Route path="/post/:id" element={<PostDetailPage />} />
        <Route
          path="/my"
          element={
            <PrivatedRoute>
              <MyPage />
            </PrivatedRoute>
          }
        />
        <Route
          path="/choice"
          element={
            <PrivatedRoute>
              <ChoicePage />
            </PrivatedRoute>
          }
        />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
      <Route
        path="/auth"
        element={
          <PrivatedRoute>
            <AuthPage />
          </PrivatedRoute>
        }
      />
      {/* <Route path="/reviews/:uuid" element={<ReviewPage />} /> */}
    </Routes>
  );
}

function App() {
  return (
    <AuthProvider>
      <ThemeProvider>
        <GlobalStyle />
        <ABTestProvider>
          <AppContent />
        </ABTestProvider>
      </ThemeProvider>
    </AuthProvider>
  );
}

export default App;
