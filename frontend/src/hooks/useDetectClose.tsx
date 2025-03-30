import { useEffect, useRef, useCallback } from 'react';

interface UseDetectCloseProps {
  onDetected: () => void;
}

export default function useDetectClose({ onDetected }: UseDetectCloseProps) {
  const ref = useRef<HTMLDivElement>(null);

  const handleOutsideClick = useCallback(
    (event: MouseEvent) => {
      if (ref.current && !ref.current.contains(event.target as Node)) {
        onDetected();
      }
    },
    [onDetected],
  );

  useEffect(() => {
    document.addEventListener('mousedown', handleOutsideClick);
    return () => {
      document.removeEventListener('mousedown', handleOutsideClick);
    };
  }, [handleOutsideClick]);

  return ref;
}
