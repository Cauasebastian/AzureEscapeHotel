import React, { createContext, useState, useContext } from "react"
// If you're using Vite and the default import causes issues, try different import methods:
// 1. Default import (should work with jwt-decode 4.0.0)
import jwt_decode from "jwt-decode"

// 2. If the above doesn't work, you could try this approach:
// import * as jwt_decode from "jwt-decode"

export const AuthContext = createContext({
  user: null,
  handleLogin: (token) => {},
  handleLogout: () => {}
})

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)

  const handleLogin = (token) => {
    try {
      const decodedUser = jwt_decode(token)
      localStorage.setItem("userId", decodedUser.sub)
      localStorage.setItem("userRole", decodedUser.roles)
      localStorage.setItem("token", token)
      console.log("Decoded user:", decodedUser)
      setUser(decodedUser)
    } catch (error) {
      console.error("Failed to decode token:", error)
    }
  }

  const handleLogout = () => {
    localStorage.removeItem("userId")
    localStorage.removeItem("userRole")
    localStorage.removeItem("token")
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, handleLogin, handleLogout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => {
  return useContext(AuthContext)
}
