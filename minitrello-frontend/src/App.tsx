import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import LoginPage from './pages/LoginPage.tsx'
import DashboardPage from './pages/DashboardPage.tsx'
import { AuthProvider } from './context/AuthContext.tsx'
import RootLayout from './components/RootLayout.tsx'
import ProtectedRoute from './components/ProtectedRoute.tsx'
import PublicRoute from './components/PublicRoute.tsx'
import AuthLayout from './components/AuthLayout.tsx'
import RegisterPage from './pages/RegisterPage.tsx'

const router = createBrowserRouter([
  {
    path: "/",
    element: <PublicRoute />,
    children: [
      {
        element: <AuthLayout />,
        children: [
          { index: true, element: <LoginPage /> },
          { path: "register", element: <RegisterPage />}
        ]
      }
    ]
  },
  {
    path: "/",
    element: <RootLayout />,
    children: [
      {
        element: <ProtectedRoute />,
        children: [
          { path: "dashboard", element: <DashboardPage /> }
        ]
      }
    ]
  }

])

export default function App() {
  return (
    <AuthProvider>
      <RouterProvider router={router} />
    </AuthProvider>
  )
}
