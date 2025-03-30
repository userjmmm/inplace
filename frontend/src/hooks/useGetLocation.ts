import { useState, useEffect } from 'react';

export default function useGetLocation() {
  const GEOLOCATION_CONFIG = {
    maximumAge: 300000,
    timeout: 5000,
  };

  const [location, setLocation] = useState<{ lat: number; lng: number } | null>(null);

  useEffect(() => {
    if (navigator.geolocation) {
      const watchId = navigator.geolocation.watchPosition(
        (position) => {
          const newCenter = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          };
          setLocation(newCenter);
        },
        (error) => {
          console.error('Error fetching location', error);
        },
        GEOLOCATION_CONFIG,
      );

      return () => navigator.geolocation.clearWatch(watchId);
    }
    console.error('Geolocation is not supported by this browser.');
    return undefined;
  }, []);

  return location;
}
