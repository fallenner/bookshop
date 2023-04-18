import { FC } from 'react';
import logoPNG from '@/assets/img/logo.png';

export const Header: FC = () => {
  return (
    <div>
      <div>
        <a href=".">
          <img src={logoPNG} />
        </a>
      </div>
    </div>
  );
};
