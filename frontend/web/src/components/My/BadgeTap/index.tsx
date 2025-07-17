import styled from 'styled-components';

export default function BadgeTap() {
  return <Wrapper>badge</Wrapper>;
}

const Wrapper = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 60px;

  @media screen and (max-width: 768px) {
    width: 100%;
    gap: 40px;
    align-items: center;
  }
`;
