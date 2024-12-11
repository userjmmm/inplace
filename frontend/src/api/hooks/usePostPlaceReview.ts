import { useMutation } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { RequestPlaceReview } from '@/types';

export const postPlaceReviewPath = (id: string) => `/places/${id}/reviews`;
const postPlaceReview = async (id: string, { likes, comments }: RequestPlaceReview) => {
  const response = await fetchInstance.post(
    postPlaceReviewPath(id),
    {
      likes,
      comments,
    },
    { withCredentials: true },
  );
  return response.data;
};

export const usePostPlaceReview = (id: string) => {
  return useMutation({
    mutationFn: ({ likes, comments }: RequestPlaceReview) => postPlaceReview(id, { likes, comments }),
  });
};
