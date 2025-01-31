import styled from 'styled-components';

import { BannerData } from '@/types';

export default function BannerItem({ id, imageUrl, isFirst }: BannerData & { isFirst: boolean }) {
  return (
    <Wrapper $isFirst={isFirst}>
      <Image src={imageUrl} alt={`배너-${id}번`} />
    </Wrapper>
  );
}
const Wrapper = styled.div<{ $isFirst: boolean }>`
  width: ${({ $isFirst }) => ($isFirst ? '100%' : '50%')};
  flex: 0 0 ${({ $isFirst }) => ($isFirst ? '100%' : '50%')};
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
