import { rest } from 'msw';
import { BASE_URL } from '@/api/instance';
import { getInfluencerPath } from '@/api/hooks/useGetMain';

const mockInfluencers = [
  {
    influencerId: 1,
    influencerName: '성시경',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '모델',
    likes: true,
  },
  {
    influencerId: 2,
    influencerName: '풍자',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '배우',
    likes: false,
  },
  {
    influencerId: 3,
    influencerName: '아이유',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '가수',
    likes: false,
  },
  {
    influencerId: 4,
    influencerName: '이영자',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '방송인',
    likes: false,
  },
  {
    influencerId: 5,
    influencerName: '정해인',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '배우',
    likes: false,
  },
  {
    influencerId: 6,
    influencerName: '황정민',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '배우',
    likes: false,
  },
  {
    influencerId: 7,
    influencerName: '히밥',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '유튜버',
    likes: false,
  },
  {
    influencerId: 8,
    influencerName: '백종원',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '유튜버',
    likes: true,
  },
  {
    influencerId: 9,
    influencerName: '안성재',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '셰프',
    likes: false,
  },
  {
    influencerId: 10,
    influencerName: '임영웅',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '배우',
    likes: false,
  },
  {
    influencerId: 11,
    influencerName: '짱구 대디',
    influencerImgUrl: 'https://via.placeholder.com/100',
    influencerJob: '패션 유튜버',
    likes: false,
  },
];

export const InfluencerHandlers = [
  rest.get(`${BASE_URL}${getInfluencerPath()}`, (req, res, ctx) => {
    const url = new URL(req.url);
    const page = parseInt(url.searchParams.get('page') ?? '0', 10);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);

    const totalElements = mockInfluencers.length;
    const totalPages = Math.ceil(totalElements / size);
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, totalElements);
    const paginatedContent = mockInfluencers.slice(startIndex, endIndex);

    return res(
      ctx.status(200),
      ctx.json({
        totalPages,
        totalElements,
        size,
        content: paginatedContent,
        number: page,
        sort: {
          empty: true,
          sorted: true,
          unsorted: true,
        },
        numberOfElements: paginatedContent.length,
        pageable: {
          offset: page * size,
          sort: {
            empty: true,
            sorted: true,
            unsorted: true,
          },
          paged: true,
          pageNumber: page,
          pageSize: size,
          unpaged: false,
        },
        first: page === 0,
        last: page === totalPages - 1,
        empty: paginatedContent.length === 0,
      }),
    );
  }),
];

export default InfluencerHandlers;
