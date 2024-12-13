import { useState, useEffect } from 'react';

export default function useGetLocation() {
  const [location, setLocation] = useState<{ lat: number; lng: number } | null>(null);

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
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
      );
    } else {
      console.error('Geolocation is not supported by this browser.');
    }
  }, []);

  return location;
}
