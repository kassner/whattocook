import { createRoot } from 'react-dom/client';

const App = () => {
  return (
    <h1>What to cook today?</h1>
  );
}

const root = createRoot(document.getElementById("app") as HTMLElement);
root.render(<App />);
