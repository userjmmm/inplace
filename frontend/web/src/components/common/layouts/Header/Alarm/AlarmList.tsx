import styled from 'styled-components';
import AlarmItem from './AlarmItem';
import { AlarmData } from '@/types';
import useTheme from '@/hooks/useTheme';

interface AlarmListProps {
  alarms: AlarmData[];
  isVisible: boolean;
}

export default function AlarmList({ alarms, isVisible }: AlarmListProps) {
  const { theme } = useTheme();
  const isDarkMode = theme === 'dark';

  if (!isVisible) return null;

  return (
    <AlarmListContainer $isDarkMode={isDarkMode}>
      <AlarmHeader $isDarkMode={isDarkMode}>
        <AlarmTitle>알림</AlarmTitle>
      </AlarmHeader>

      <AlarmScrollContainer $isDarkMode={isDarkMode}>
        {alarms.length > 0 ? (
          alarms.map((alarm) => <AlarmItem key={alarm.alarmId} {...alarm} />)
        ) : (
          <EmptyMessage $isDarkMode={isDarkMode}>알림이 없습니다.</EmptyMessage>
        )}
      </AlarmScrollContainer>
    </AlarmListContainer>
  );
}

const AlarmListContainer = styled.div<{ $isDarkMode: boolean }>`
  position: absolute;
  top: calc(100% + 6px);
  right: -50px;
  width: 330px;
  max-height: 400px;
  background-color: ${(props) => (props.$isDarkMode ? '#1a1a1a' : '#ffffff')};
  border: 1px solid ${(props) => (props.$isDarkMode ? '#2a2a2a' : '#e1e5e9')};
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 21;
  overflow: hidden;
`;

const AlarmHeader = styled.div<{ $isDarkMode: boolean }>`
  padding: 16px;
  border-bottom: 1px solid ${(props) => (props.$isDarkMode ? '#2a2a2a' : '#f0f0f0')};
  background-color: ${(props) => (props.$isDarkMode ? '#1a1a1a' : '#ffffff')};
`;

const AlarmTitle = styled.h3`
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: inherit;
`;

const AlarmScrollContainer = styled.div<{ $isDarkMode: boolean }>`
  max-height: 320px;
  overflow-y: auto;
  padding: 0 10px;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background: ${(props) => (props.$isDarkMode ? '#525252' : '#8e8e8e')};
    border-radius: 3px;
  }
`;

const EmptyMessage = styled.div<{ $isDarkMode: boolean }>`
  padding: 32px 16px;
  text-align: center;
  color: ${(props) => (props.$isDarkMode ? '#666' : '#999')};
  font-size: 14px;
`;
