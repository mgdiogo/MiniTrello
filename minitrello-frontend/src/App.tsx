import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import HomePage from './pages/Home.tsx';

const router = createBrowserRouter([
  {/* Root route with a layout component will be declared here
      Example declaration:
      {
        path: "/",
        element: <RootLayout />,
        children: [
          { element: <HomePage /> }
        ]
      }
  */},

  { index: true, element: <HomePage /> }
]);

export default function App() {
  return <RouterProvider router={router} />;
}