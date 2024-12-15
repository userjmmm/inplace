import { useEffect, useMemo, useCallback, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { useInView } from 'react-intersection-observer';
import PlaceItem from '@/components/Map/PlaceSection/PlaceItem';
import { PlaceData, LocationData, PageableData } from '@/types';
import Loading from '@/components/common/layouts/Loading';
import NoItem from '@/components/common/layouts/NoItem';
import { useGetInfinitePlaceList } from '@/api/hooks/useGetInfinitePlaceList';

interface PlaceSectionProps {
  mapBounds: LocationData;
  filters: {
    categories: string[];
    influencers: string[];
    location: { main: string; sub?: string; lat?: number; lng?: number };
  };
  onPlacesUpdate: (places: PlaceData[]) => void;
  center: { lat: number; lng: number };
  shouldFetchPlaces: boolean;
  onFetchComplete: () => void;
  initialLocation: boolean;
}

export default function PlaceSection({
  mapBounds,
  filters,
  onPlacesUpdate,
  center,
  shouldFetchPlaces,
  onFetchComplete,
}: PlaceSectionProps) {
  const navigate = useNavigate();
  const sectionRef = useRef<HTMLDivElement>(null); // 무한 스크롤을 위한 ref와 observer 설정
  const { ref: loadMoreRef, inView } = useInView({
    // useInView = Intersection Oberser API를 react hook으로 구현한 것
    root: sectionRef.current,
    rootMargin: '0px',
    threshold: 0, // 요소가 조금이라도 보이면 감지
  });

  // 데이터 fetching hook
  const { data, isLoading, isError, error, fetchNextPage, hasNextPage, isFetchingNextPage } = useGetInfinitePlaceList(
    {
      location: mapBounds,
      filters,
      center,
      size: 10, // 한 페이지에 보여줄 아이템 개수; 변경하며 api 잘 받아오는지 확인 가능
    },
    shouldFetchPlaces,
  );

  const filteredPlaces = useMemo(() => {
    if (!data?.pages) return [];

    return data.pages.flatMap((page: PageableData<PlaceData>) => {
      return page.content.filter((place: PlaceData) => {
        const categoryMatch = filters.categories.length === 0 || filters.categories.includes(place.category);
        const influencerMatch = filters.influencers.length === 0 || filters.influencers.includes(place.influencerName);
        const locationMatch = (() => {
          if (!filters.location.main) return true;
          const mainMatch =
            place.address.address1.includes(filters.location.main) ||
            place.address.address2.includes(filters.location.main);
          const subMatch = filters.location.sub
            ? place.address.address2.includes(filters.location.sub) ||
              (place.address.address3 && place.address.address3.includes(filters.location.sub))
            : true;
          return mainMatch && subMatch;
        })();
        return categoryMatch && influencerMatch && locationMatch;
      });
    });
  }, [data, filters]);

  // 스크롤이 LoadMoreTrigger에 도달하면 다음 페이지 로드
  useEffect(() => {
    if (inView && hasNextPage && !isFetchingNextPage) {
      fetchNextPage();
    }
  }, [inView, hasNextPage, isFetchingNextPage, fetchNextPage]);

  useEffect(() => {
    if (!isLoading && shouldFetchPlaces) {
      onFetchComplete();
    }
  }, [isLoading, shouldFetchPlaces, onFetchComplete]);

  useEffect(() => {
    if (!isLoading) {
      onPlacesUpdate(filteredPlaces);
    }
  }, [filteredPlaces, onPlacesUpdate, isLoading]);

  const handlePlaceClick = useCallback(
    (placeId: number) => {
      navigate(`/detail/${placeId}`);
    },
    [navigate],
  );

  if (isLoading && !isFetchingNextPage) {
    return (
      <SectionContainer>
        <LoadingContainer>
          <Loading size={50} />
        </LoadingContainer>
      </SectionContainer>
    );
  }

  if (isError) {
    return (
      <SectionContainer>
        <ErrorContainer>Error: {(error as Error).message}</ErrorContainer>
      </SectionContainer>
    );
  }

  return (
    <SectionContainer ref={sectionRef}>
      {filteredPlaces.length === 0 ? (
        <NoItem message="장소 정보가 없어요!" height={400} />
      ) : (
        <ContentContainer>
          <PlacesGrid>
            {filteredPlaces.map((place) => (
              <PlaceItem key={place.placeId} {...place} onClick={() => handlePlaceClick(place.placeId)} />
            ))}
          </PlacesGrid>
          {(hasNextPage || isFetchingNextPage) && (
            <LoadMoreTrigger ref={loadMoreRef}>
              <Loading size={30} />
            </LoadMoreTrigger>
          )}
        </ContentContainer>
      )}
    </SectionContainer>
  );
}

const SectionContainer = styled.div`
  height: 600px;
  width: 100%;
  overflow-y: auto;
  padding-right: 4px;
  box-sizing: content-box;
  &::-webkit-scrollbar {
    width: 8px;
    color: #1f1f1f;
  }

  &::-webkit-scrollbar-thumb {
    background-color: #1f1f1f;
    border-radius: 4px;
    border: none;
  }

  &::-webkit-scrollbar-thumb:hover {
    background-color: #1f1f1f;
    width: 8px;
  }

  &::-webkit-scrollbar-track {
    background-color: transparent;
  }
`;

const ContentContainer = styled.div`
  width: 100%;
`;

const PlacesGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
`;

const LoadMoreTrigger = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  margin-top: 20px;
`;

const LoadingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
`;

const ErrorContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  color: red;
`;