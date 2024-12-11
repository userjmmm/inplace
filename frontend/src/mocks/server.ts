import { setupServer } from 'msw/node';
import { mainHandlers } from './mainHandlers';
import { detailHandlers } from './detailHandlers';
import { mapHandlers } from './mapHandlers';
import { myHandlers } from './myPageHandlers';
import { searchHandlers } from './searchHandlers';

const server = setupServer(...mainHandlers, ...detailHandlers, ...mapHandlers, ...myHandlers, ...searchHandlers);
export default server;
