import { setupWorker } from 'msw';
import { mainHandlers } from './mainHandlers';
import { detailHandlers } from './detailHandlers';
import { mapHandlers } from './mapHandlers';
import { myHandlers } from './myPageHandlers';
import { InfluencerHandlers } from './influencerHandlers';
import { searchHandlers } from './searchHandlers';

export const worker = setupWorker(
  ...InfluencerHandlers,
  ...mainHandlers,
  ...detailHandlers,
  ...mapHandlers,
  ...myHandlers,
  ...searchHandlers,
);
export default worker;
