import { useRef } from 'react';
import styled from 'styled-components';
import useInfiniteScroll from '@/hooks/useInfiniteScroll';
import MyReview from '../UserReview';
import { useGetUserReview } from '@/api/hooks/useGetUserReview';

export default function ActiveTap() {
  const reviewRef = useRef<HTMLDivElement>(null);
  const { data: reviews, fetchNextPage, hasNextPage, isFetchingNextPage } = useGetUserReview(10);
  const reviewLoadMoreRef = useInfiniteScroll({
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  });

  return (
    <Wrapper>
      <MyReview
        mainText="나의 리뷰"
        items={reviews.pages.flatMap((page) => page.content)}
        loadMoreRef={reviewLoadMoreRef}
        sectionRef={reviewRef}
        hasNextPage={hasNextPage}
        isFetchingNextPage={isFetchingNextPage}
      />
    </Wrapper>
  );
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
