import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null) // { token, nome, role, aeroportoIata }

  function login(userData) {
    window.__glider_token__ = userData.token
    setUser(userData)
  }

  function logout() {
    window.__glider_token__ = null
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}