import { useState, useCallback, useEffect, useRef } from 'react';
import { CustomOverlayMap, Map, MapMarker, MarkerClusterer } from 'react-kakao-maps-sdk';
import styled from 'styled-components';
import { TbCurrentLocation } from 'react-icons/tb';
import { GrPowerCycle } from 'react-icons/gr';
import Button from '@/components/common/Button';
import { LocationData, PlaceData } from '@/types';
import { useGetAllMarkers } from '@/api/hooks/useGetAllMarkers';
import InfoWindow from '@/components/InfluencerInfo/InfluencerMapTap/InfoWindow';
import OriginMarker from '@/assets/images/OriginMarker.png';
import SelectedMarker from '@/assets/images/InplaceMarker.png';
import { Text } from '@/components/common/typography/Text';
import nowLocation from '@/assets/images/now_location.webp';
import Loading from '@/components/common/layouts/Loading';
import useMapActions from '@/hooks/Map/useMapAction';
import useMarkerData from '@/hooks/Map/useMarkerData';
import useIsMobile from '@/hooks/useIsMobile';

interface MapWindowProps {
  center: { lat: number; lng: number };
  onBoundsChange: (bounds: LocationData) => void;
  onCenterChange: (center: { lat: number; lng: number }) => void;
  filters: {
    categories: string[];
    influencers: string[];
    placeName: string;
    region: string;
    location: { main: string; sub?: string; lat?: number; lng?: number }[];
  };
  placeData: PlaceData[];
  selectedPlaceId: number | null;
  onPlaceSelect: (placeId: number | null) => void;
  isListExpanded?: boolean;
  onListExpand?: () => void;
}

export default function MapWindow({
  center,
  onBoundsChange,
  onCenterChange,
  filters,
  placeData,
  selectedPlaceId,
  onPlaceSelect,
  isListExpanded,
  onListExpand,
}: MapWindowProps) {
  const GEOLOCATION_CONFIG = {
    maximumAge: 30000,
    timeout: 5000,
  };
  const DEFAULT_MAP_ZOOM_LEVEL = 4;

  const mapRef = useRef<kakao.maps.Map | null>(null);
  const [isMapReady, setIsMapReady] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [mapCenter, setMapCenter] = useState(center || { lat: 37.5665, lng: 126.978 });
  const [mapBound, setMapBound] = useState<LocationData>({
    topLeftLatitude: 0,
    topLeftLongitude: 0,
    bottomRightLatitude: 0,
    bottomRightLongitude: 0,
  });
  const [userLocation, setUserLocation] = useState<{ lat: number; lng: number } | null>(null);
  const [showSearchButton, setShowSearchButton] = useState(false);
  const isMobile = useIsMobile();
  const { moveMapToMarker, handleResetCenter } = useMapActions({ mapRef, onPlaceSelect });
  const { markerInfo, handleMarkerClick, handleMapClick } = useMarkerData({
    selectedPlaceId,
    placeData,
    onPlaceSelect,
    moveMapToMarker,
    mapRef,
  });

  const originSize = isMobile ? 26 : 34;
  const userLocationSize = isMobile ? 16 : 24;

  const { data: markers = [] } = useGetAllMarkers({
    location: mapBound,
    filters,
    center: mapCenter,
  });
  // const markers = AllmarkerData?.marker || [];
  const selectedMarker = markers.find((m) => m.placeId === selectedPlaceId);

  // useEffect(() => {
  //   if (AllmarkerData?.map && mapRef.current) {
  //     const { latitude, longitude, level } = AllmarkerData.map;
  //     const newCenter = new kakao.maps.LatLng(latitude, longitude);

  //     mapRef.current.setCenter(newCenter);
  //     mapRef.current.setLevel(level);

  //     setMapCenter({ lat: latitude, lng: longitude });
  //   }
  // }, [AllmarkerData]);

  const fetchMarkers = useCallback(() => {
    if (!mapRef.current) return;

    const bounds = mapRef.current.getBounds();
    const currentCenter = mapRef.current.getCenter();

    const newBounds: LocationData = {
      topLeftLatitude: bounds.getNorthEast().getLat(),
      topLeftLongitude: bounds.getSouthWest().getLng(),
      bottomRightLatitude: bounds.getSouthWest().getLat(),
      bottomRightLongitude: bounds.getNorthEast().getLng(),
    };

    onPlaceSelect(null);
    setMapCenter({ lat: currentCenter.getLat(), lng: currentCenter.getLng() });
    setMapBound(newBounds);

    onCenterChange({ lat: currentCenter.getLat(), lng: currentCenter.getLng() });
    onBoundsChange(newBounds);
  }, [mapRef]);

  useEffect(() => {
    if (!isMapReady) return;
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          setIsLoading(false);
          const userLoc = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          };
          setUserLocation(userLoc);
          mapRef.current?.setCenter(new kakao.maps.LatLng(userLoc.lat, userLoc.lng));
          fetchMarkers();
        },
        (error) => {
          console.error('Geolocation error:', error);
          setIsLoading(false);
          if (error.code === error.PERMISSION_DENIED) {
            alert('위치 권한이 차단되었습니다. 위치를 수동으로 설정하세요.');
          } else if (error.code === error.POSITION_UNAVAILABLE) {
            alert('위치 정보를 가져올 수 없습니다.');
          } else if (error.code === error.TIMEOUT) {
            alert('위치 정보 요청이 시간 초과되었습니다. 다시 시도해주세요.');
          }
        },
        GEOLOCATION_CONFIG,
      );
    } else {
      setIsLoading(false);
      console.warn('Geolocation is not supported by this browser.');
    }
  }, [isMapReady]);

  useEffect(() => {
    if (mapRef.current && center) {
      const position = new kakao.maps.LatLng(center.lat, center.lng);
      mapRef.current.setCenter(position);

      const bounds = mapRef.current.getBounds();
      const newBounds: LocationData = {
        topLeftLatitude: bounds.getNorthEast().getLat(),
        topLeftLongitude: bounds.getSouthWest().getLng(),
        bottomRightLatitude: bounds.getSouthWest().getLat(),
        bottomRightLongitude: bounds.getNorthEast().getLng(),
      };
      onPlaceSelect(null);
      mapRef.current.setLevel(DEFAULT_MAP_ZOOM_LEVEL);
      setMapCenter(center);
      setMapBound(newBounds);
      onBoundsChange(newBounds);
    }
  }, [center, mapRef]);

  // 초기 선택 시에만 이동하도록
  useEffect(() => {
    if (selectedPlaceId) {
      const marker = markers.find((m) => m.placeId === selectedPlaceId);
      if (marker) {
        moveMapToMarker(marker.latitude, marker.longitude);
      }
    }
  }, [selectedPlaceId, moveMapToMarker]);

  const handleSearchNearby = useCallback(() => {
    fetchMarkers();
    setShowSearchButton(false);
  }, [fetchMarkers]);

  return (
    <MapContainer>
      {isLoading ? (
        <LoadingWrapper>
          <Loading />
          <Text size="s" weight="normal" variant="white">
            내 위치 찾는 중...
          </Text>
        </LoadingWrapper>
      ) : null}
      {showSearchButton && (
        <ButtonContainer>
          <Button
            aria-label="around_btn"
            onClick={handleSearchNearby}
            variant="white"
            size="small"
            style={{
              fontSize: isMobile ? '12px' : '14px',
              borderRadius: '20px',
              padding: isMobile ? '16px' : '18px',
              backgroundColor: 'rgba(0, 0, 0, 0.7)',
              color: 'white',
              gap: '8px',
            }}
          >
            <GrPowerCycle />이 위치에서 장소 보기
          </Button>
        </ButtonContainer>
      )}
      <Map
        center={mapCenter}
        style={{ width: '100%', height: isMobile ? 'auto' : '570px', aspectRatio: isMobile ? '1' : 'auto' }}
        level={DEFAULT_MAP_ZOOM_LEVEL}
        onCreate={(mapInstance) => {
          mapRef.current = mapInstance;
          setIsMapReady(true);
        }}
        onCenterChanged={() => {
          setShowSearchButton(true);
        }}
        onZoomChanged={() => {
          setShowSearchButton(true);
        }}
        onClick={handleMapClick}
      >
        {userLocation && (
          <MapMarker
            position={userLocation}
            image={{
              src: nowLocation,
              size: { width: userLocationSize, height: userLocationSize },
            }}
          />
        )}
        <MarkerClusterer averageCenter minLevel={10} minClusterSize={2}>
          {markers.map((place) => (
            <MapMarker
              key={place.placeId}
              zIndex={selectedPlaceId === place.placeId ? 999 : 1}
              onClick={(marker) => {
                handleMarkerClick(place.placeId, marker);
              }}
              position={{
                lat: place.latitude,
                lng: place.longitude,
              }}
              image={{
                src: selectedPlaceId === place.placeId ? SelectedMarker : OriginMarker,
                size: {
                  width: selectedPlaceId === place.placeId ? originSize + 14 : originSize,
                  height: selectedPlaceId === place.placeId ? originSize + 14 : originSize,
                },
              }}
            />
          ))}
        </MarkerClusterer>
        {selectedPlaceId !== null && selectedMarker && markerInfo && (
          <CustomOverlayMap
            zIndex={100}
            position={{
              lat: selectedMarker.latitude,
              lng: selectedMarker.longitude,
            }}
            clickable
          >
            <InfoWindow data={markerInfo} />
          </CustomOverlayMap>
        )}
      </Map>
      <ResetButtonContainer>
        <StyledBtn
          aria-label="reset_btn"
          onClick={() => userLocation && handleResetCenter(userLocation)}
          variant="white"
          size="small"
        >
          <TbCurrentLocation size={20} />
        </StyledBtn>
      </ResetButtonContainer>
      {!isListExpanded && (
        <ListViewButton onClick={onListExpand}>
          <Text size="xs" weight="normal" variant="white">
            목록 보기
          </Text>
        </ListViewButton>
      )}
    </MapContainer>
  );
}

const MapContainer = styled.div`
  position: relative;
  width: 100%;
  padding-bottom: 20px;
  color: black;
`;
const LoadingWrapper = styled.div`
  position: absolute;
  z-index: 99;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  background-color: #29292963;

  > div {
    padding: 4px 16px;
    height: auto;
    transform: translateY(-50%);
  }

  .loader {
    border-left-color: #e6e6e6;
    padding: 0px;
  }
`;

const ButtonContainer = styled.div`
  position: absolute;
  top: 5%;
  left: 50%;
  transform: translateX(-50%);
  z-index: 5;
`;

const ResetButtonContainer = styled.div`
  position: absolute;
  bottom: 46px;
  right: 30px;
  z-index: 10;

  @media screen and (max-width: 768px) {
    bottom: 34px;
    right: 14px;
  }
`;

const StyledBtn = styled(Button)`
  width: 40px;
  height: 40px;
  box-shadow: 1px 1px 2px #707070;

  @media screen and (max-width: 768px) {
    width: 28px;
    height: 28px;
    box-shadow: 1px 1px 1px #707070;
    svg {
      width: 16px;
      height: 16px;
    }
  }
`;

const ListViewButton = styled.button`
  display: none;

  @media screen and (max-width: 768px) {
    display: flex;
    position: absolute;
    bottom: 10%;
    left: 50%;
    transform: translateX(-50%);
    background: rgba(0, 0, 0, 0.7);
    border: none;
    border-radius: 20px;
    padding: 8px 16px;
    cursor: pointer;
    z-index: 10;
    align-items: center;
    gap: 4px;
  }
`;
