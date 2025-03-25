import { useCallback } from 'react';

export default function useMapActions(mapRef: React.MutableRefObject<kakao.maps.Map | null>) {
  const PAN_DELAY_MS = 100;
  const MIN_ZOOM_LEVEL = 4;
  const MAX_ZOOM_THRESHOLD = 10;
  const BASE_OFFSET_Y = -0.007;

  const moveMapToMarker = useCallback((latitude: number, longitude: number) => {
    if (!mapRef.current) return;

    const levelMultiplier = mapRef.current.getLevel() > MAX_ZOOM_THRESHOLD ? 2 : 1;
    const offsetY = (BASE_OFFSET_Y * levelMultiplier) / 3;
    const position = new kakao.maps.LatLng(latitude - offsetY, longitude);

    if (mapRef.current.getLevel() > MIN_ZOOM_LEVEL) {
      mapRef.current.setLevel(MIN_ZOOM_LEVEL, { anchor: position, animate: true });
    }

    setTimeout(() => mapRef.current?.panTo(position), PAN_DELAY_MS);
  }, []);

  const handleResetCenter = useCallback((userLocation: { lat: number; lng: number }) => {
    if (mapRef.current && userLocation) {
      mapRef.current.setCenter(new kakao.maps.LatLng(userLocation.lat, userLocation.lng));
      mapRef.current.setLevel(4);
    }
  }, []);

  return { moveMapToMarker, handleResetCenter };
}
