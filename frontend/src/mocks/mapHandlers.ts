import { rest } from 'msw';
import { BASE_URL } from '@/api/instance';
import { PlaceData } from '@/types';

const dummyInfluencers = [
  { influencerName: '성시경' },
  { influencerName: '풍자' },
  { influencerName: '아이유' },
  { influencerName: '이영자' },
  { influencerName: '정해인' },
  { influencerName: '황정민' },
  { influencerName: '히밥' },
  { influencerName: '백종원' },
  { influencerName: '안성재' },
  { influencerName: '임영웅' },
];

const dummyPlaces: PlaceData[] = [
  {
    placeId: 1,
    placeName: '료코',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대학로',
    },
    category: 'RESTAURANT',
    influencerName: '성시경',
    longitude: '35.123',
    latitude: '135.11',
    likes: true,
    menuImgUrl: 'https://via.placeholder.com/500',
  },
  {
    placeId: 2,
    placeName: '긴자료코 홍대본점',
    address: {
      address1: '서울',
      address2: '마포구',
      address3: '서교동',
    },
    category: 'KOREAN',
    influencerName: '임영웅',
    longitude: '126.9314925',
    latitude: '37.5666478',
    likes: true,
    menuImgUrl: 'https://via.placeholder.com/500',
  },
  {
    placeId: 3,
    placeName: '맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-9',
    },
    category: 'JAPANESE',
    influencerName: '풍자',
    longitude: '128.6101069',
    latitude: '35.8857457',
    likes: false,
    menuImgUrl: 'https://via.placeholder.com/500',
  },
  {
    placeId: 4,
    placeName: '4맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현로19길 38-1',
    },
    category: 'WESTERN',
    influencerName: '풍자',
    longitude: '128.6201071',
    latitude: '35.8857457',
    likes: false,
    menuImgUrl: 'https://via.placeholder.com/500',
  },
  {
    placeId: 5,
    placeName: '5맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-9',
    },
    category: 'KOREAN',
    influencerName: '풍자',
    longitude: '128.6101073',
    latitude: '35.8857457',
    likes: false,
    menuImgUrl: 'https://via.placeholder.com/500',
  },
  {
    placeId: 6,
    placeName: '6맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-9',
    },
    category: 'CAFE',
    influencerName: '풍자',
    longitude: '128.6101069',
    latitude: '35.8857500',
    likes: false,
    menuImgUrl: 'https://via.placeholder.com/500',
  },
  {
    placeId: 7,
    placeName: '7맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-9',
    },
    category: '맛집',
    influencerName: '풍자',
    longitude: '128.6101608',
    latitude: '35.8857457',
    likes: false,
    menuImgUrl: 'https://via.placeholder.com/500',
  },
  {
    placeId: 8,
    placeName: '8맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-7',
    },
    category: '맛집',
    influencerName: '풍자',
    longitude: '128.6101702',
    latitude: '35.8857457',
    likes: false,
    menuImgUrl: 'https://via.placeholder.com/500',
  },
  {
    placeId: 9,
    placeName: '맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-8',
    },
    category: '맛집',
    influencerName: '풍자',
    longitude: '128.6101513',
    latitude: '35.8857457',
    likes: false,
    menuImgUrl: 'https://via.placeholder.com/500',
  },
  {
    placeId: 10,
    placeName: '10맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-10',
    },
    category: '맛집',
    influencerName: '풍자',
    longitude: '128.6101169',
    latitude: '35.8857457',
    likes: false,
    menuImgUrl: 'https://via.placeholder.com/500',
  },
  {
    placeId: 11,
    placeName: '11맘스터치 대구대현점',
    address: {
      address1: '대구',
      address2: '북구',
      address3: '대현동 119-11',
    },
    category: '맛집',
    influencerName: '풍자',
    longitude: '128.9386',
    latitude: '35.9733',
    likes: false,
    menuImgUrl: 'https://via.placeholder.com/500',
  },
];

export const mapHandlers = [
  rest.get(`${BASE_URL}/places`, (req, res, ctx) => {
    const url = new URL(req.url);
    const page = parseInt(url.searchParams.get('page') ?? '0', 10);
    const size = parseInt(url.searchParams.get('size') ?? '10', 10);

    const totalElements = dummyPlaces.length;
    const totalPages = Math.ceil(totalElements / size);
    const startIndex = page * size;
    const endIndex = Math.min(startIndex + size, totalElements);
    const paginatedContent = dummyPlaces.slice(startIndex, endIndex);
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
  rest.post(`${BASE_URL}/places/likes`, (req, res, ctx) => {
    const { placeId, likes } = req.body as { placeId: string; likes: boolean };
    return res(
      ctx.status(200),
      ctx.json({
        placeId,
        likes,
      }),
    );
  }),
  rest.get(`${BASE_URL}/influencers/names`, (_, res, ctx) => {
    return res(ctx.status(200), ctx.json(dummyInfluencers));
  }),
];

export default mapHandlers;
