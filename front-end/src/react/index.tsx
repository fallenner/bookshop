import { createRoot } from 'react-dom/client';
import { App } from './App';

document.body.innerHTML = '<div id="app"></div>';

const rootNode = document.getElementById('app');

if (rootNode) {
  createRoot(rootNode).render(<App />);
}
