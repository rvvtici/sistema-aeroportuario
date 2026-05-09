import { useAuth } from '../context/AuthContext'
import { LoginPage } from '../pages/LoginPage'

export function ProtectedRoute({ children }) {
  const { user } = useAuth()
  if (!user) return <LoginPage />
  return children
}