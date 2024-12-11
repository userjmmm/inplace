import { useEffect, useRef, useCallback } from 'react';

interface UseDetectCloseProps {
  onDetected: () => void;
}

export default function useDetectClose({ onDetected }: UseDetectCloseProps) {
  const ref = useRef<HTMLDivElement>(null);

  const handleClickOutside = useCallback(
    (event: MouseEvent) => {
      if (ref.current && !ref.current.contains(event.target as Node)) {
        onDetected();
      }
    },
    [onDetected],
  );

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [handleClickOutside]);

  return ref;
}
