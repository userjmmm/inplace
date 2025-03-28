import { useState, useMemo, useCallback } from 'react';
import styled from 'styled-components';
import MapWindow from '@/components/Map/MapWindow';
import PlaceSection from '@/components/Map/PlaceSection';
import Chip from '@/components/common/Chip';
import locationOptions from '@/utils/constants/LocationOptions';
import categoryOptions from '@/utils/constants/CategoryOptions';
import useGetDropdownName from '@/api/hooks/useGetDropdownName';
import useTouchDrag from '@/hooks/Map/useTouchDrag';
import useMapState from '@/hooks/Map/useMapState';
import MapSearchBar from '@/components/Map/MapSearchBar';
import DropdownFilterBar, { FilterBarItem } from '@/components/Map/\bDropdownFilterBar';
import useIsMobile from '@/hooks/useIsMobile';

type SelectedOption = {
  main: string;
  sub?: string;
  lat?: number;
  lng?: number;
};

export default function MapPage() {
  const { data: influencerOptions } = useGetDropdownName();
  const [selectedInfluencers, setSelectedInfluencers] = useState<string[]>([]);
  const [selectedLocations, setSelectedLocations] = useState<SelectedOption[]>([]);
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [selectedPlaceName, setSelectedPlaceName] = useState<string>('');
  const [isDropdownOpened, setIsDropdownOpened] = useState(false);
  const [isChangedLocation, setIsChangedLocation] = useState(false);
  const isMobile = useIsMobile();
  const {
    center,
    setCenter,
    mapBounds,
    isListExpanded,
    selectedPlaceId,
    placeData,
    setIsListExpanded,
    handleBoundsChange,
    handleCenterChange,
    handlePlaceSelect,
    handleGetPlaceData,
  } = useMapState();
  const { translateY, setTranslateY, handleTouchStart, handleTouchMove, handleTouchEnd } =
    useTouchDrag(setIsListExpanded);

  const filters = useMemo(
    () => ({
      categories: selectedCategories,
      influencers: selectedInfluencers,
      location: selectedLocations,
      regions: selectedLocations.map((loc) => (loc.sub ? `${loc.main}-${loc.sub}` : loc.main)),
    }),
    [selectedCategories, selectedInfluencers, selectedLocations],
  );

  const filtersWithPlaceName = useMemo(
    () => ({
      categories: selectedCategories,
      influencers: selectedInfluencers,
      location: selectedLocations,
      placeName: selectedPlaceName,
      regions: selectedLocations.map((loc) => (loc.sub ? `${loc.main}-${loc.sub}` : loc.main)),
    }),
    [selectedCategories, selectedInfluencers, selectedLocations, selectedPlaceName],
  );

  const handleLocationChange = useCallback((value: SelectedOption) => {
    setSelectedLocations((prev) => {
      // 중복 생성 방지
      const isDuplicate = prev.some((loc) => loc.main === value.main && loc.sub === value.sub);
      if (isDuplicate) return prev;

      if (value.sub === '전체' || !value.sub) {
        const hasAll = prev.some((loc) => loc.main === value.main && loc.sub === '전체');
        if (hasAll) return prev;
        return [...prev, value];
      }

      if (value.sub) {
        return [...prev, value];
      }
      return prev;
    });

    if (value.lat && value.lng) {
      setCenter({ lat: value.lat, lng: value.lng });
      setIsChangedLocation(true);
    }
  }, []);

  const handleInfluencerChange = useCallback((value: { main: string }) => {
    setSelectedInfluencers((prev) => {
      // 이미 선택된 인플루언서인 경우 추가하지 않음
      if (prev.includes(value.main)) return prev;
      return [...prev, value.main];
    });
  }, []);

  const handleCategoryChange = useCallback((value: { main: string }) => {
    setSelectedCategories((prev) => {
      if (prev.includes(value.main)) return prev;
      return [...prev, value.main];
    });
  }, []);

  const handleLocationClear = useCallback((locationToRemove: SelectedOption) => {
    setSelectedLocations((prev) =>
      prev.filter((location) => !(location.main === locationToRemove.main && location.sub === locationToRemove.sub)),
    );
  }, []);

  const handleInfluencerClear = useCallback((influencerToRemove: string) => {
    setSelectedInfluencers((prev) => prev.filter((influencer) => influencer !== influencerToRemove));
  }, []);

  const handleCategoryClear = useCallback((categoryToRemove: string) => {
    setSelectedCategories((prev) => prev.filter((category) => category !== categoryToRemove));
  }, []);

  const handleListExpand = useCallback(() => {
    setIsListExpanded((prev) => {
      const newExpandedState = !prev;
      setTranslateY(newExpandedState ? 0 : window.innerHeight);
      return newExpandedState;
    });
  }, []);

  const handleToggleDropdown = () => {
    if (isMobile) {
      setIsDropdownOpened((prev) => !prev);
    }
  };

  const dropdownItems: FilterBarItem[] = [
    {
      type: 'dropdown',
      id: 'location',
      props: {
        options: locationOptions,
        multiLevel: true,
        onChange: handleLocationChange,
        placeholder: '지역',
        type: 'location',
        width: 90,
        selectedOptions: selectedLocations,
      },
    },
    { type: 'separator', id: 'sep1' },
    {
      type: 'dropdown',
      id: 'influencer',
      props: {
        options: influencerOptions,
        onChange: handleInfluencerChange,
        placeholder: '인플루언서',
        type: 'influencer',
        width: 140,
        selectedOptions: selectedInfluencers,
      },
    },
    { type: 'separator', id: 'sep2' },
    {
      type: 'dropdown',
      id: 'category',
      props: {
        options: categoryOptions,
        onChange: handleCategoryChange,
        placeholder: '카테고리',
        type: 'category',
        width: 120,
        selectedOptions: selectedCategories,
      },
    },
  ];

  return (
    <PageContainer>
      <Wrapper>
        <FilterContainer>
          <MobileFilterContainer onClick={handleToggleDropdown}>
            <MapSearchBar setCenter={setCenter} setSelectedPlaceName={setSelectedPlaceName} />
          </MobileFilterContainer>
          <DesktopDropdownSection>
            <DropdownFilterBar items={dropdownItems} />
          </DesktopDropdownSection>
        </FilterContainer>
        {isDropdownOpened && (
          <MobileDropdownSection $isDropdownOpened={isDropdownOpened}>
            <MapSearchBar setCenter={setCenter} setSelectedPlaceName={setSelectedPlaceName} />
            <DropdownFilterBar items={dropdownItems} />
            <CloseBtn onClick={() => setIsDropdownOpened(false)}>닫기</CloseBtn>
          </MobileDropdownSection>
        )}
        <Chip
          selectedLocations={selectedLocations}
          selectedInfluencers={selectedInfluencers}
          selectedCategories={selectedCategories}
          onClearLocation={handleLocationClear}
          onClearInfluencer={handleInfluencerClear}
          onClearCategory={handleCategoryClear}
        />
      </Wrapper>
      <MapWindow
        center={center}
        onBoundsChange={handleBoundsChange}
        onCenterChange={handleCenterChange}
        filters={filters}
        filtersWithPlaceName={filtersWithPlaceName}
        placeData={placeData}
        selectedPlaceId={selectedPlaceId}
        isChangedLocation={isChangedLocation}
        setIsChangedLocation={setIsChangedLocation}
        onPlaceSelect={handlePlaceSelect}
        isListExpanded={isListExpanded}
        onListExpand={handleListExpand}
      />
      <PlaceSectionDesktop>
        <PlaceSection
          mapBounds={mapBounds}
          filters={filters}
          filtersWithPlaceName={filtersWithPlaceName}
          center={center}
          onGetPlaceData={handleGetPlaceData}
          onPlaceSelect={handlePlaceSelect}
          selectedPlaceId={selectedPlaceId}
        />
      </PlaceSectionDesktop>
      <MobilePlaceSection
        $isExpanded={isListExpanded}
        onClick={() => isListExpanded && setIsListExpanded(false)}
        $translateY={translateY}
        onTouchStart={handleTouchStart}
        onTouchMove={handleTouchMove}
        onTouchEnd={handleTouchEnd}
      >
        <DragHandle />
        <PlaceSection
          mapBounds={mapBounds}
          filters={filters}
          filtersWithPlaceName={filtersWithPlaceName}
          center={center}
          onGetPlaceData={handleGetPlaceData}
          onPlaceSelect={handlePlaceSelect}
          selectedPlaceId={selectedPlaceId}
          isListExpanded={isListExpanded}
          onListExpand={handleListExpand}
        />
      </MobilePlaceSection>
    </PageContainer>
  );
}

const PageContainer = styled.div`
  padding: 6px 0;
  @media screen and (max-width: 768px) {
    position: relative;
    width: 100%;
    align-items: center;
    touch-action: none;
  }
`;

const Wrapper = styled.div`
  @media screen and (max-width: 768px) {
    display: flex;
    flex-direction: column;
    align-items: center;
    > * {
      width: 90%;
    }
  }
`;

const FilterContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;

  @media screen and (max-width: 768px) {
    width: 90%;
    padding-top: 12px;
    z-index: 10;
    gap: 10px;
    flex-wrap: wrap;
  }
`;

const PlaceSectionDesktop = styled.div`
  margin-top: 10px;
  @media screen and (max-width: 768px) {
    display: none;
  }
`;

const MobilePlaceSection = styled.div<{ $translateY: number; $isExpanded: boolean }>`
  display: ${({ $isExpanded }) => ($isExpanded ? 'block' : 'none')};

  @media screen and (max-width: 768px) {
    display: block;
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    transform: translateY(${(props) => props.$translateY}px);
    height: 80vh;
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#3c3c3c' : '#fafafa')};
    z-index: 90;
    border-top-left-radius: 16px;
    border-top-right-radius: 16px;
    transition: transform 0.3s ease-out;
    touch-action: none;
  }
`;

const DragHandle = styled.div`
  width: 100%;
  height: 20px;
  padding: 12px 0;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  background-color: transparent;
  cursor: grab;
  touch-action: none;

  &:after {
    content: '';
    width: 40px;
    height: 4px;
    background-color: #666;
    border-radius: 2px;
  }
`;

const MobileFilterContainer = styled.div`
  @media screen and (max-width: 768px) {
    width: 100%;
  }
`;

const MobileDropdownSection = styled.div<{ $isDropdownOpened: boolean }>`
  display: none;

  @media screen and (max-width: 768px) {
    display: flex;
    flex-direction: column;
    position: fixed;
    align-items: end;
    gap: 10px;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    background-color: ${({ theme }) => theme.backgroundColor};
    z-index: 101;
    padding: 20px;
    box-sizing: border-box;
  }
`;

const DesktopDropdownSection = styled.div`
  @media screen and (max-width: 768px) {
    display: none;
  }
`;

const CloseBtn = styled.button`
  display: none;

  @media screen and (max-width: 768px) {
    display: block;
    position: fixed;
    bottom: 10%;
    width: 45%;
    font-size: 14px;
    padding: 10px 0px;
    border-radius: 4px;

    background: none;
    color: #55ebff;
    border: 1px solid #55ebff;
  }
`;
