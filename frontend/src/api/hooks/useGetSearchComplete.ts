import { useQuery } from '@tanstack/react-query';

import { fetchInstance } from '../instance';
import { SearchComplete } from '@/types';

export const getSearchCompletePath = () => `/search/complete`;
const getSearchComplete = async (value: string) => {
  const params = new URLSearchParams({
    value: value.toString(),
  });
  const response = await fetchInstance.get<SearchComplete[]>(`${getSearchCompletePath()}?${params}`);
  return response.data;
};

export const useGetSearchComplete = (value: string) => {
  return useQuery({
    queryKey: ['searchComplete', value],
    queryFn: () => getSearchComplete(value),
    staleTime: 1000 * 60 * 5,
    enabled: !!value,
  });
};
