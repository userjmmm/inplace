import styled from 'styled-components';
import BaseLayout from '@/components/common/BaseLayout';
import MainBanner from '@/components/Main/MainBanner';

import { useGetMain } from '@/api/hooks/useGetMain';
import SearchBar from '@/components/common/SearchBar';
import useAuth from '@/hooks/useAuth';
import { useGetLogoutVideo } from '@/api/hooks/useGetLogoutVideo';
import { useGetMyInfluencerVideo } from '@/api/hooks/useGetMyInfluencerVideo';
import useGetLocation from '@/hooks/useGetLocation';
import { useGetAroundVideo } from '@/api/hooks/useGetAroundVideo';

export default function MainPage() {
  const { isAuthenticated } = useAuth();
  const location = useGetLocation();

  const [{ data: bannerData }, { data: influencersData }] = useGetMain();
  const [{ data: coolVideoData }, { data: newVideoData }] = useGetLogoutVideo(!isAuthenticated);
  const { data: myInfluencerVideoData } = useGetMyInfluencerVideo(!!isAuthenticated);
  const { data: aroundVideoData } = useGetAroundVideo(
    location?.lat ?? 37.5665,
    location?.lng ?? 126.978,
    !!isAuthenticated && !!location?.lat && !!location?.lng,
  );

  return (
    <Wrapper>
      <SearchBar placeholder="인플루언서, 장소를 검색해주세요!" />
      <MainBanner items={bannerData} />
      <BaseLayout
        type="influencer"
        mainText="인플루언서"
        SubText=" 가 방문한 장소를 찾아볼까요?"
        items={influencersData.content}
      />
      {isAuthenticated ? (
        <>
          <BaseLayout
            type="spot"
            prevSubText="내 "
            mainText="인플루언서"
            SubText="가 방문한 그곳!"
            items={myInfluencerVideoData || []}
          />
          {location?.lat && location?.lng && (
            <BaseLayout
              type="spot"
              prevSubText="내 "
              mainText="주변"
              SubText="에 있는 그곳!"
              items={aroundVideoData || []}
            />
          )}
        </>
      ) : (
        <>
          <BaseLayout type="spot" prevSubText="지금 " mainText="쿨" SubText=" 한 그곳!" items={coolVideoData || []} />
          <BaseLayout type="spot" mainText="새로" SubText=" 등록된 그곳!" items={newVideoData || []} />
        </>
      )}
    </Wrapper>
  );
}
const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 50px;
`;