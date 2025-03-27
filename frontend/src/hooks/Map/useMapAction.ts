import { useCallback } from 'react';

interface UseMapActionsProps {
  onPlaceSelect: (placeId: number | null) => void;
  mapRef: React.MutableRefObject<kakao.maps.Map | null>;
}

export default function useMapActions({ mapRef, onPlaceSelect }: UseMapActionsProps) {
  const PAN_DELAY_MS = 100;
  const DEFAULT_ZOOM_LEVEL = 4;
  const MIN_ZOOM_LEVEL = 2;
  const BASE_OFFSET_Y = -0.002;
  const SMALL_ZOOM_OFFSET_Y = -0.0007;

  const moveMapToMarker = useCallback((latitude: number, longitude: number) => {
    if (!mapRef.current) return;

    const currentLevel = mapRef.current.getLevel();

    if (currentLevel === 1) {
      mapRef.current.setLevel(MIN_ZOOM_LEVEL, { animate: true });
    }

    if (currentLevel > DEFAULT_ZOOM_LEVEL) {
      mapRef.current.setLevel(DEFAULT_ZOOM_LEVEL, { animate: true });
    }

    const offsetY = currentLevel < DEFAULT_ZOOM_LEVEL ? SMALL_ZOOM_OFFSET_Y : BASE_OFFSET_Y;
    const position = new kakao.maps.LatLng(latitude - offsetY, longitude);
    setTimeout(() => {
      mapRef.current?.panTo(position);
    }, PAN_DELAY_MS);
  }, []);

  const handleResetCenter = useCallback((userLocation: { lat: number; lng: number }) => {
    if (mapRef.current && userLocation) {
      mapRef.current.setCenter(new kakao.maps.LatLng(userLocation.lat, userLocation.lng));
      mapRef.current.setLevel(DEFAULT_ZOOM_LEVEL);
      onPlaceSelect(null);
    }
  }, []);

  return { moveMapToMarker, handleResetCenter };
}
