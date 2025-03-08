import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { FaMapMarkerAlt } from 'react-icons/fa';
import { ImSpoonKnife } from 'react-icons/im';
import { Text } from '@/components/common/typography/Text';
import { AddressInfo, MarkerInfo, PlaceData } from '@/types';
import FallbackImage from '@/components/common/Items/FallbackImage';

type Props = {
  data: MarkerInfo | PlaceData;
};

const getFullAddress = (addr: AddressInfo) => {
  return [addr.address1, addr.address2, addr.address3].filter(Boolean).join(' ');
};

const extractYoutubeId = (url: string) => {
  const match = url?.match(/(?:https?:\/\/)?(?:www\.)?youtu(?:be\.com\/watch\?v=|\.be\/)([\w-]*)(&(amp;)?[\w?=]*)?/);
  const youtubeId = match && match[1] ? match[1] : null;
  return `https://img.youtube.com/vi/${youtubeId}/mqdefault.jpg`;
};

export default function InfoWindow({ data }: Props) {
  const navigate = useNavigate();
  const isYoutubeUrl = data.videos[0].videoUrl?.includes('youtu');

  const handleClickInfo = (event: React.MouseEvent<HTMLDivElement>) => {
    event.stopPropagation();
    navigate(`/detail/${data.placeId}`);
  };

  return (
    <Wrapper>
      <Info>
        <Img>
          <FallbackImage src={isYoutubeUrl ? extractYoutubeId(data.videos[0].videoUrl) : ''} alt={data.placeName} />
        </Img>
        <TextInfo>
          <Text size="xxs" weight="bold">
            {data.placeName}
          </Text>
          <Text size="xxs" weight="normal">
            {data.videos[0].influencerName}
          </Text>
          <Text size="xxs" weight="normal" variant="#869c9d">
            <FaMapMarkerAlt size={12} />
            {data.address ? getFullAddress(data.address) : '주소 정보가 없습니다'}
          </Text>
          <Text size="xxs" weight="normal" variant="#869c9d">
            <ImSpoonKnife size={12} />
            {data.category}
          </Text>
        </TextInfo>
      </Info>
      <DetailBtn onClick={(e: React.MouseEvent<HTMLDivElement>) => handleClickInfo(e)}>상세보기</DetailBtn>
      {/* <CloseBtn aria-label="close_btn" onClick={() => onClose()}>
        x
      </CloseBtn> */}
    </Wrapper>
  );
}
const Wrapper = styled.div`
  position: absolute;
  bottom: 50px;
  left: 20px;
  margin-left: -130px;
  width: 220px;
  overflow: hidden;
  background-color: #ffffff;
  border-radius: 14px;
  display: flex;
  flex-direction: column;
  box-shadow: 1px 1px 1px #b3b3b3;

  @media screen and (max-width: 768px) {
    width: 180px;
    margin-left: -110px;
    bottom: 40px;
  }
`;
const Info = styled.div`
  width: 100%;
  display: flex;
  gap: 14px;
  box-sizing: border-box;
  align-items: center;
  flex-direction: column;

  @media screen and (max-width: 768px) {
    gap: 10px;
  }
`;
const Img = styled.div`
  width: 100%;
  aspect-ratio: 16 / 9;
  overflow: hidden;
  height: auto;
`;
const TextInfo = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 16px;
  padding-top: 0px;
  box-sizing: border-box;

  color: #4d4d4d;
  overflow: hidden;
  span {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  svg {
    margin-right: 4px;
  }

  @media screen and (max-width: 768px) {
    padding: 10px;
    padding-top: 0px;
    gap: 4px;
  }
`;

const DetailBtn = styled.div`
  width: 100%;
  height: 36px;
  background-color: #ecfdff;
  color: black;
  text-align: center;
  align-content: center;
  font-size: 14px;
  cursor: pointer;
  &:visited {
    color: black;
  }
  &:hover {
    background-color: #d0ecf0;
    transition: 0.3s;
  }
  @media screen and (max-width: 768px) {
    height: 26px;
    font-size: 12px;
  }
`;
