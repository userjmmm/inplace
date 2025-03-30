import { useEffect, useCallback, useRef } from 'react';
import styled from 'styled-components';
import { useInView } from 'react-intersection-observer';
import PlaceItem from '@/components/Map/PlaceSection/PlaceItem';
import { PlaceData, LocationData, FilterParams } from '@/types';
import Loading from '@/components/common/layouts/Loading';
import NoItem from '@/components/common/layouts/NoItem';
import { useGetInfinitePlaceList } from '@/api/hooks/useGetInfinitePlaceList';
import usePlaceList from '@/hooks/Map/usePlaceList';
import { useGetInfiniteSearchPlaceList } from '@/api/hooks/useGetInfiniteSearchPlaceList';

interface PlaceSectionProps {
  mapBounds: LocationData;
  filters: {
    categories: string[];
    influencers: string[];
    regions: string[];
    location: { main: string; sub?: string; lat?: number; lng?: number }[];
  };
  filtersWithPlaceName: FilterParams;
  center: { lat: number; lng: number };
  onGetPlaceData: (data: PlaceData[]) => void;
  onPlaceSelect: (placeId: number) => void;
  selectedPlaceId: number | null;
  isListExpanded?: boolean;
  onListExpand?: () => void;
}

export default function PlaceSection({
  mapBounds,
  filters,
  filtersWithPlaceName,
  center,
  onGetPlaceData,
  onPlaceSelect,
  selectedPlaceId,
  isListExpanded,
  onListExpand,
}: PlaceSectionProps) {
  const sectionRef = useRef<HTMLDivElement>(null); // 무한 스크롤을 위한 ref와 observer 설정
  const previousPlacesRef = useRef<PlaceData[]>([]);

  const { ref: loadMoreRef, inView } = useInView({
    root: sectionRef.current,
    rootMargin: '0px',
    threshold: 0, // 요소가 조금이라도 보이면 감지
  });

  // 데이터 fetching hook
  const {
    data: placeList,
    isLoading: isLoadingPlaceList,
    isError: isErrorPlaceList,
    error: errorPlaceList,
    fetchNextPage: fetchNextPagePlaceList,
    hasNextPage: hasNextPagePlaceList,
    isFetchingNextPage: isFetchingNextPagePlaceList,
  } = useGetInfinitePlaceList(
    {
      location: mapBounds,
      filters,
      center,
      size: 10, // 한 페이지에 보여줄 아이템 개수; 변경하며 api 잘 받아오는지 확인 가능
    },
    !filtersWithPlaceName.placeName,
  );

  const {
    data: searchPlaceList,
    isLoading: isLoadingSearchPlaceList,
    isError: isErrorSearchPlaceList,
    error: errorSearchPlaceList,
    fetchNextPage: fetchNextPageSearchPlaceList,
    hasNextPage: hasNextPageSearchPlaceList,
    isFetchingNextPage: isFetchingNextPageSearchPlaceList,
  } = useGetInfiniteSearchPlaceList(
    {
      filters: filtersWithPlaceName,
      size: 10,
    },
    !!filtersWithPlaceName.placeName,
  );

  const isLoading = filtersWithPlaceName.placeName ? isLoadingSearchPlaceList : isLoadingPlaceList;
  const isError = filtersWithPlaceName.placeName ? isErrorSearchPlaceList : isErrorPlaceList;
  const error = (filtersWithPlaceName.placeName ? errorSearchPlaceList : errorPlaceList) as Error;

  const { filteredPlaces } = usePlaceList({
    data: filtersWithPlaceName.placeName ? searchPlaceList : placeList,
    onGetPlaceData,
  });

  useEffect(() => {
    if (
      inView &&
      (filtersWithPlaceName.placeName ? hasNextPageSearchPlaceList : hasNextPagePlaceList) &&
      !(filtersWithPlaceName.placeName ? isFetchingNextPageSearchPlaceList : isFetchingNextPagePlaceList)
    ) {
      if (placeList && !filtersWithPlaceName.placeName) {
        fetchNextPagePlaceList();
      }
      if (searchPlaceList && filtersWithPlaceName.placeName) {
        fetchNextPageSearchPlaceList();
      }
    }
  }, [
    inView,
    hasNextPagePlaceList,
    hasNextPageSearchPlaceList,
    isFetchingNextPagePlaceList,
    isFetchingNextPageSearchPlaceList,
    fetchNextPagePlaceList,
    fetchNextPageSearchPlaceList,
    placeList,
    searchPlaceList,
  ]);

  const handlePlaceClick = useCallback(
    (placeId: number) => {
      onPlaceSelect(placeId);
      if (isListExpanded && onListExpand) {
        onListExpand();
      }
    },
    [onPlaceSelect, isListExpanded, onListExpand],
  );

  if (
    isLoading &&
    !isFetchingNextPagePlaceList &&
    !isFetchingNextPageSearchPlaceList &&
    previousPlacesRef.current.length === 0
  ) {
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
              <PlaceItem
                key={place.placeId}
                {...place}
                onClick={() => handlePlaceClick(place.placeId)}
                isSelected={selectedPlaceId === place.placeId}
              />
            ))}
          </PlacesGrid>
          {filtersWithPlaceName.placeName
            ? hasNextPagePlaceList || isFetchingNextPagePlaceList
            : (hasNextPageSearchPlaceList || isFetchingNextPageSearchPlaceList) && (
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
  }

  &::-webkit-scrollbar-thumb {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1f1f1f' : '#8e8e8e')};
    border-radius: 4px;
    border: none;
  }

  &::-webkit-scrollbar-track {
    background-color: transparent;
  }

  @media screen and (max-width: 768px) {
    height: 100%;
    padding-right: 0;
  }
`;

const ContentContainer = styled.div`
  width: 100%;

  @media screen and (max-width: 768px) {
    width: 100%;
    padding: 0 10px 40px 16px;
    box-sizing: border-box;
  }
`;

const PlacesGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;

  @media screen and (max-width: 768px) {
    grid-template-columns: 1fr;
    gap: 20px;
  }
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
