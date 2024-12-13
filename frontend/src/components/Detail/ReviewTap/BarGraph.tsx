import styled from 'styled-components';
import { PlaceLikes } from '@/types';

export default function BarGraph({ like, dislike }: PlaceLikes) {
  const total = like + dislike;
  const likePercentage = total > 0 ? (like / total) * 100 : 0;
  const dislikePercentage = total > 0 ? (dislike / total) * 100 : 0;

  return (
    <GraphContainer>
      <Bar color="#FE7373" percentage={likePercentage}>
        <Label $isLike>{like}</Label>
      </Bar>
      <Bar color="#6f6cff" percentage={dislikePercentage}>
        <Label $isLike={false}>{dislike}</Label>
      </Bar>
    </GraphContainer>
  );
}

const GraphContainer = styled.div`
  display: flex;
  align-items: center;
  width: 900px;
  height: 44px;
  border-radius: 1000px;
  overflow: hidden;
  position: relative;
`;

const Bar = styled.div<{ color: string; percentage: number }>`
  height: 100%;
  background-color: ${(props) => props.color};
  width: ${(props) => props.percentage}%;
`;

const Label = styled.div<{ $isLike: boolean }>`
  position: absolute;
  top: 0;
  ${({ $isLike }) => ($isLike ? 'left: 20px;' : 'right: 20px;')};
  line-height: 40px;

  font-size: 18px;
  color: white;
  font-weight: bold;
`;
