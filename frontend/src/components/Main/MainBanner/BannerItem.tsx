import styled from 'styled-components';

import { BannerData } from '@/types';

export default function BannerItem({ id, imageUrl }: BannerData) {
  return (
    <Wrapper>
      <Image src={imageUrl} alt={`배너-${id}번`} />
    </Wrapper>
  );
}
const Wrapper = styled.div`
  width: 100%;
  flex: 0 0 100%;
  height: 400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  position: relative;
`;

const Image = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;
