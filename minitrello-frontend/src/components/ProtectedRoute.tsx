import { Navigate, Outlet } from "react-router-dom"
import useAuth from "../hooks/useAuth"

// This component acts as a gate for protected pages
// If the user is authenticated render the page
// If not silently redirect to login
export default function ProtectedRoute() {
  const { isAuthenticated } = useAuth()

  // replace={true} means the user can't click "back" to get to the protected page
  return isAuthenticated ? <Outlet /> : <Navigate to="/" replace />
}