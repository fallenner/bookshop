import {
  createBrowserRouter,
  createRoutesFromElements,
  Route,
} from 'react-router-dom';
import { Home } from '../pages/Home';

const routes = createBrowserRouter(
  createRoutesFromElements(<Route path="/" element={<Home />} />)
);

export default routes;
