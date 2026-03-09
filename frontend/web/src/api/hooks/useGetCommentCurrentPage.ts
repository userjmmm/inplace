import { useQuery } from '@tanstack/react-query';

import { getFetchInstance } from '@inplace-frontend-monorepo/shared';

export const getCommentCurrentPagePath = (postId: string, commentId: number) =>
  `/posts/${postId}/comments/${commentId}/position`;

export const getCommentCurrentPage = async (postId: string, commentId: number) => {
  const response = await getFetchInstance().get<number>(getCommentCurrentPagePath(postId, commentId), {
    withCredentials: true,
  });
  return response.data;
};

export const useGetCommentCurrentPage = (postId: string, commentId: number) => {
  return useQuery({
    queryKey: ['commentCurrentPage', postId, commentId],
    queryFn: () => getCommentCurrentPage(postId, commentId),
  });
};
