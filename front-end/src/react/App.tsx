import { FC, StrictMode } from 'react';
import routes from './routes';
import { RouterProvider } from 'react-router-dom';
import { Header } from './components/Header';

export const App: FC = () => {
  return (
    <StrictMode>
      <Header />
      <RouterProvider router={routes} />
    </StrictMode>
  );
};
