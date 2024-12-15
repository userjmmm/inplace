import { useState } from 'react';
import styled from 'styled-components';
import { AiFillDislike, AiFillLike } from 'react-icons/ai';
import { AddressInfo } from '@/types';
import { Text } from '@/components/common/typography/Text';
import FallbackImage from '@/components/common/Items/FallbackImage';

interface CommentStepProps {
  isLiked: boolean | null;
  onBack: () => void;
  onSubmit: (content: string) => void;
  placeInfo: {
    placeName: string;
    address: AddressInfo;
    influencerName: string;
    menuInfos: {
      menuImgUrls: string[];
    };
  };
}

interface RatingDisplayProps {
  buttonType: 'like' | 'dislike';
}

export default function CommentStep({ isLiked, onBack, onSubmit, placeInfo }: CommentStepProps) {
  const [content, setContent] = useState('');

  return (
    <StepContainer>
      <TitleSection>
        <Text size="l" weight="normal" variant="white">
          어떤 점이 {isLiked ? '좋으셨나요?' : '아쉬우셨나요?'}
        </Text>
      </TitleSection>

      <TextWrapper>
        <Text size="l" weight="bold" variant="white">
          한 줄 리뷰
        </Text>
        <Text size="l" weight="normal" variant="white">
          를 작성해주세요.&nbsp;
        </Text>
        <Text size="l" weight="normal" style={{ color: '#c6c6c6' }}>
          (선택)
        </Text>
      </TextWrapper>

      <PlaceSection>
        <ImageWrapper>
          <FallbackImage src={placeInfo.menuInfos.menuImgUrls[0]} alt="Restaurant Menu" />
        </ImageWrapper>
        <PlaceInfo>
          <TextWrapper className="name">
            <Text size="m" weight="bold" style={{ color: '#c6c6c6' }}>
              {placeInfo.placeName}
            </Text>
          </TextWrapper>
          <TextWrapper className="address">
            <Text size="xs" weight="normal" style={{ color: '#c6c6c6' }}>
              {placeInfo.address.address1} {placeInfo.address.address2} {placeInfo.address.address3}
            </Text>
          </TextWrapper>
          <TextWrapper className="influencer">
            <Text size="xs" weight="normal" variant="white">
              {placeInfo.influencerName}
            </Text>
          </TextWrapper>
        </PlaceInfo>
      </PlaceSection>

      <ReviewSection>
        <RatingDisplay buttonType={isLiked ? 'like' : 'dislike'}>
          {isLiked ? <AiFillLike size={40} /> : <AiFillDislike size={40} />}
          <ButtonText>{isLiked ? '좋았어요' : '아쉬워요'}</ButtonText>
        </RatingDisplay>

        <ReviewTextArea
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="근거 없는 후기, 비방, 명예훼손은 표현은 삭제될 수 있습니다."
          maxLength={200}
        />
      </ReviewSection>
      <CharCount>{content.length} / 200</CharCount>

      <ButtonSection>
        <CancelButton onClick={onBack}>취소</CancelButton>
        <SubmitButton onClick={() => onSubmit(content)}>등록</SubmitButton>
      </ButtonSection>
    </StepContainer>
  );
}

const StepContainer = styled.div`
  animation: fadeIn 0.3s ease-in-out;
  width: min(100%, 500px);
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  margin: 0 auto;
  @keyframes fadeIn {
    from {
      opacity: 0;
    }
    to {
      opacity: 1;
    }
  }
`;

const TitleSection = styled.div`
  margin-top: 30%;
`;

const TextWrapper = styled.div`
  margin: 0.5rem 0;
`;

const PlaceSection = styled.div`
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  gap: 2rem;
  width: 100%;
  margin-top: 3rem;
  margin-bottom: 5rem;
  position: relative;
`;

const ImageWrapper = styled.div`
  width: 10rem;
  height: 10rem;
  object-fit: cover;
  border-radius: 0.5rem;
`;

const PlaceInfo = styled.div`
  flex-direction: column;
`;

const ReviewSection = styled.div`
  display: flex;
  gap: 1rem;
  align-items: flex-start;
`;

const RatingDisplay = styled.div<RatingDisplayProps>`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  padding: 0.7rem;
  color: ${(props) => (props.buttonType === 'like' ? '#D85B53' : '#595BD4')};
`;

const ButtonText = styled.span`
  font-size: 0.7rem;
  color: white;
`;

const ReviewTextArea = styled.textarea`
  width: 100%;
  height: 6rem;
  background-color: transparent;
  border: 1px solid #333;
  border-radius: 0.5rem;
  padding: 1rem;
  color: white;
  font-size: 0.875rem;
  resize: none;

  &::placeholder {
    color: #666;
  }

  &:focus {
    outline: none;
    border-color: #5fe2ff;
  }
`;

const CharCount = styled.div`
  text-align: right;
  color: #999;
  font-size: 0.875rem;
  margin-top: 0.5rem;
`;

const ButtonSection = styled.div`
  display: flex;
  gap: 1rem;
  justify-content: center;
  margin-top: 3rem;
`;

const Button = styled.button`
  flex: 1;
  padding: 0.75rem;
  border-radius: 0.5rem;
  font-size: 1rem;
  transition: opacity 0.2s ease;

  &:hover {
    opacity: 0.8;
  }
`;

const CancelButton = styled(Button)`
  background-color: transparent;
  border: 1px solid #333;
  color: white;
`;

const SubmitButton = styled(Button)`
  background-color: #5fe2ff;
  border: none;
  color: white;
`;