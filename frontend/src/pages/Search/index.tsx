import { useSearchParams } from 'react-router-dom';
import styled from 'styled-components';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { Text } from '@/components/common/typography/Text';
import SearchBar from '@/components/common/SearchBar';
import BaseLayout from '@/components/common/BaseLayout';
import { useGetSearchData } from '@/api/hooks/useGetSearchData';

export default function SearchPage() {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('query') || '';

  const [{ data: influencersData }, { data: VideoData }, { data: places }] = useGetSearchData(query);

  return (
    <Wrapper>
      <SearchBar placeholder="인플루언서, 장소를 검색해주세요!" />
      <Paragraph weight="normal" size="m" variant="white">
        <Text weight="bold" size="m" variant="mint">
          {`${query} `}
        </Text>
        검색 결과
      </Paragraph>
      <SplitLine />
      <BaseLayout type="influencer" mainText="" SubText="인플루언서" items={influencersData || []} />
      <SplitLine />
      <BaseLayout type="spot" mainText="" SubText="바로 그곳" items={VideoData || []} />
      <SplitLine />
      <BaseLayout type="place" mainText="" SubText="관련 장소" items={places || []} />
    </Wrapper>
  );
}
const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 50px;
`;
const SplitLine = styled.div`
  border-bottom: 1px solid #595959;
`;
