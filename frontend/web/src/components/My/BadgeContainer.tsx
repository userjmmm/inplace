import styled from 'styled-components';
import { useGetAllBadge } from '@/api/hooks/useGetAllBadge';
import { Text } from '../common/typography/Text';

export default function BadgeContainer() {
  const { data: badgeList = [] } = useGetAllBadge();
  return (
    <Wrapper>
      {badgeList.map((badge) => {
        return (
          <BadgeWrapper $isOwned={badge.isOwned} $isSelected={badge.isSelected}>
            <BadgeImage key={badge.id} src={badge.imgUrl} alt={badge.name} />
            <Text size="xs" weight="normal">
              {badge.name}
            </Text>
          </BadgeWrapper>
        );
      })}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  display: grid;
  gap: 14px;
  grid-template-columns: repeat(4, 1fr);
  justify-items: center;
  @media screen and (max-width: 768px) {
    width: 90%;
    gap: 12px;
    grid-template-columns: repeat(2, 1fr);
  }
`;

const BadgeWrapper = styled.div<{ $isOwned: boolean; $isSelected: boolean }>`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center;
  border-radius: 6px;
  padding: 12px 16px;
  box-sizing: border-box;
  filter: ${({ $isOwned }) => (!$isOwned ? 'grayscale(100%) opacity(40%)' : 'none')};
  transition: filter 0.3s ease;
  border: ${({ $isSelected }) => ($isSelected ? '1px solid #55EBFF' : 'none')};
  background-color: #1f1f1f;
`;
const BadgeImage = styled.img`
  width: 70%;
`;
