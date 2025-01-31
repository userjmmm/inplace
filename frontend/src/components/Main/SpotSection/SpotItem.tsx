import { FaMapMarkerAlt } from 'react-icons/fa';
import { Link } from 'react-router-dom';

import styled from 'styled-components';

import { Paragraph } from '@/components/common/typography/Paragraph';

import useExtractYoutubeVideoId from '@/libs/youtube/useExtractYoutube';
import { SpotData } from '@/types';
import FallbackImage from '@/components/common/Items/FallbackImage';
import BasicImage from '@/assets/images/basic-image.png';

export default function SpotItem({ videoId, videoAlias, videoUrl, place }: SpotData) {
  const extractedVideoId = useExtractYoutubeVideoId(videoUrl || '');
  const thumbnailUrl = videoUrl ? `https://img.youtube.com/vi/${extractedVideoId}/maxresdefault.jpg` : BasicImage;

  return (
    <Wrapper to={`/detail/${place.placeId}`}>
      <ImageWrapper>
        <FallbackImage src={thumbnailUrl} alt={String(videoId)} />
      </ImageWrapper>
      <Paragraph size="m" weight="bold" variant="white">
        {videoAlias}
      </Paragraph>
      <Paragraph size="xs" weight="normal" variant="white">
        <FaMapMarkerAlt size={20} color="#55EBFF" />
        {place.placeName}
      </Paragraph>
    </Wrapper>
  );
}
const Wrapper = styled(Link)`
  width: 340px;
  display: flex;
  flex-direction: column;
  align-content: end;
  line-height: 30px;

  svg {
    margin-right: 2px;
  }
`;

const ImageWrapper = styled.div`
  width: 340px;
  aspect-ratio: 16 / 9;
  margin-bottom: 10px;
  border-radius: 6px;
  overflow: hidden;
`;
