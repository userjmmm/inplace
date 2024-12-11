import styled from 'styled-components';
import ChoiceItem from '@/components/common/Items/ChoiceItem';
import { InfluencerData } from '@/types';
import NoItem from '@/components/common/layouts/NoItem';

interface ChoiceListProps {
  items: InfluencerData[];
  onToggleLike: (influencerId: number, isLiked: boolean) => void;
  selectedInfluencers: Set<number>;
}

export default function ChoiceList({ items, onToggleLike, selectedInfluencers }: ChoiceListProps) {
  return items.length === 0 ? (
    <NoItem message="인플루언서 정보가 없어요!" height={350} alignItems="center" />
  ) : (
    <GridContainer>
      {items.map((influencer) => {
        return (
          <ChoiceItem
            key={influencer.influencerId}
            influencerId={influencer.influencerId}
            influencerName={influencer.influencerName}
            influencerImgUrl={influencer.influencerImgUrl}
            influencerJob={influencer.influencerJob}
            onToggleLike={onToggleLike}
            isSelected={selectedInfluencers.has(influencer.influencerId)}
          />
        );
      })}
    </GridContainer>
  );
}

const GridContainer = styled.div`
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 26px;
`;
