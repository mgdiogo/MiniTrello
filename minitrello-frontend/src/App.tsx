import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import LoginPage from './pages/LoginPage.tsx';

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

  { index: true, element: <LoginPage /> } // TODO: Check if user is logged in and either redirect to dashboard or show login page
]);

export default function App() {
  return <RouterProvider router={router} />;
}